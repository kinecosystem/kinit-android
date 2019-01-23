package org.kinecosystem.kinit.viewmodel.backup

import android.databinding.ObservableBoolean
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.blockchain.Wallet
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.server.NetworkServices
import org.kinecosystem.kinit.server.api.BackupApi
import org.kinecosystem.kinit.util.Scheduler
import org.kinecosystem.kinit.util.isValidEmail
import org.kinecosystem.kinit.view.backup.UIActions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

const val INVALID_HINT_ID = -1
const val QUESTIONS_COUNT = 2

class BackupModel(val uiActions: UIActions) : AdapterView.OnItemSelectedListener, TextWatcher {

    enum class BackupState {
        Welcome, Question, Summary, QRCode, Confirm, Complete
    }

    @Inject
    lateinit var networkServices: NetworkServices

    @Inject
    lateinit var wallet: Wallet

    @Inject
    lateinit var scheduler: Scheduler

    @Inject
    lateinit var analytics: Analytics

    @Inject
    lateinit var userRepository: UserRepository

    var isQuestionSelected = ObservableBoolean(false)
    var isClickable = ObservableBoolean(false)
    var isNextEnabled = ObservableBoolean(false)
    var titles: Array<String> = listOf("Next Question").toTypedArray()
    private var isAnswerValid = ObservableBoolean(false)
    private var currentAnswer: String? = null
    private var emailAddress: String? = null
    private var encryptedAccountStr: String? = null
    private var currentQuestion: BackupApi.BackUpQuestion? = null
    private val questionsAndAnswers: MutableList<Pair<BackupApi.BackUpQuestion, String>> = mutableListOf()
    private var step = 0

    init {
        KinitApplication.coreComponent.inject(this)
    }

    override fun afterTextChanged(answer: Editable?) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(input: CharSequence?, p1: Int, p2: Int, p3: Int) {
        input?.let {
            when (getState()) {
                BackupState.Question -> {
                    currentAnswer = if (it.trim().length >= 4) {
                        isAnswerValid.set(true)
                        it.toString().trim()
                    } else {
                        isAnswerValid.set(false)
                        null
                    }
                    isNextEnabled.set(isQuestionSelected.get() && isAnswerValid.get())
                }
                BackupState.QRCode -> {
                    emailAddress = if (it.isValidEmail()) {
                        isNextEnabled.set(true)
                        it.toString()
                    } else {
                        isNextEnabled.set(false)
                        null
                    }
                }
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(p0: AdapterView<*>?, view: View?, index: Int, p3: Long) {
        onQuestionSelected(getHints()[index])
    }

    fun retrieveHints() {
        networkServices.backupService.retrieveHints()
    }

    fun onNext() {
        performStateJob()
    }

    fun onResumeFragment() {
        when (getState()) {
            BackupState.Welcome -> {
                analytics.logEvent(Events.Analytics.ViewBackupIntroPage())
            }
            BackupState.Question -> {
                analytics.logEvent(Events.Analytics.ViewBackupFlowPage("Security question " + step))
            }
            BackupState.Summary -> {
                analytics.logEvent(Events.Analytics.ViewBackupFlowPage("Security questions confirmation"))
            }
            BackupState.QRCode -> {
                analytics.logEvent(Events.Analytics.ViewBackupFlowPage("Send Email"))
            }
            BackupState.Confirm -> {
                analytics.logEvent(Events.Analytics.ViewBackupFlowPage("Email confirmation"))
            }
            BackupState.Complete -> {
                analytics.logEvent(Events.Analytics.ViewBackupCompletedPage())
            }
        }
    }

    fun getTitle() = when (step - 1) {
        in 0 until titles.size -> titles[step - 1]
        else -> "Next Question"
    }

    fun getState(): BackupState {
        return when (step) {
            in 1..QUESTIONS_COUNT -> BackupState.Question
            QUESTIONS_COUNT + 1 -> BackupState.Summary
            QUESTIONS_COUNT + 2 -> BackupState.QRCode
            QUESTIONS_COUNT + 3 -> BackupState.Confirm
            QUESTIONS_COUNT + 4 -> BackupState.Complete
            else -> {
                reset()
                BackupState.Welcome
            }
        }
    }

    fun getHints(): List<BackupApi.BackUpQuestion> {
        Log.d("BackupModel", "get hints ${userRepository.backUpHints.size} ${userRepository.backUpHints}")
        val tmpList = userRepository.backUpHints.toMutableList()
        for (pair in questionsAndAnswers) {
            if (tmpList.contains(pair.first)) {
                tmpList.remove(pair.first)
            }
        }
        tmpList.add(0, BackupApi.BackUpQuestion(INVALID_HINT_ID, "Choose Question"))
        return tmpList.toList()
    }

    fun onBack() {
        if (step <= QUESTIONS_COUNT + 1) {
            if (questionsAndAnswers.size > 0) {
                questionsAndAnswers.removeAt(questionsAndAnswers.lastIndex)
            }
            isQuestionSelected.set(false)
            currentAnswer = null
            currentQuestion = null
        }
        step--

    }

    fun getQuestion(index: Int): String {
        return questionsAndAnswers[index].first.hint
    }

    fun getAnswer(index: Int): String {
        return questionsAndAnswers[index].second
    }

    fun answersFilledCount(): Int = questionsAndAnswers.size

    private fun performStateJob() {
        isClickable.set(false)
        when (getState()) {
            BackupState.Question -> {
                saveQuestionAnswer()
                onMoveNextStep()
                logNextStepEvent("Security question " + (step - 1))
            }
            BackupState.Summary -> {
                initBackupAccountStr()
                logNextStepEvent("Security questions confirmation")
            }
            BackupState.QRCode -> {
                sendEmailDataToServer()
                logNextStepEvent("Send Email")
            }
            BackupState.Confirm -> {
                sendChosenQuestions()
                logNextStepEvent("Email confirmation")
            }
            BackupState.Welcome -> {
                reset()
                onMoveNextStep()
            }
        }
    }

    private fun logNextStepEvent(eventParam: String) {
        analytics.logEvent(Events.Analytics.ClickCompletedStepButtonOnBackupFlowPage(eventParam))
    }

    private fun initBackupAccountStr() {
        var passphrase = ""
        questionsAndAnswers.forEach {
            passphrase += it.second
        }
        scheduler.executeOnBackground {
            encryptedAccountStr = wallet.exportAccountToStr(passphrase)
            scheduler.post {
                if (encryptedAccountStr != null) {
                    onMoveNextStep()
                } else {
                    uiActions.showErrorAlert()
                }
            }
        }
    }

    private fun sendEmailDataToServer() {
        emailAddress?.let { address ->
            encryptedAccountStr?.let { encryptedStr ->
                networkServices.backupService.updateBackupDataTo(address, encryptedStr, object : Callback<BackupApi.StatusResponse> {
                    override fun onFailure(call: Call<BackupApi.StatusResponse>?, t: Throwable?) {
                        uiActions.showErrorAlert()
                    }

                    override fun onResponse(call: Call<BackupApi.StatusResponse>?, response: Response<BackupApi.StatusResponse>?) {
                        response?.let {
                            if (it.isSuccessful) {
                                onMoveNextStep()
                            } else {
                                uiActions.showErrorAlert()
                            }
                        }
                    }

                })
                emailAddress = null
                isNextEnabled.set(false)
            }
        }
    }

    private fun onMoveNextStep() {
        step++
        uiActions.replaceFragment()
        isClickable.set(true)
    }

    private fun sendChosenQuestions() {
        var list: MutableList<Int> = mutableListOf()
        questionsAndAnswers.forEach {
            list.add(it.first.id)
        }
        networkServices.backupService.updateHints(list, object : Callback<BackupApi.StatusResponse> {
            override fun onFailure(call: Call<BackupApi.StatusResponse>?, t: Throwable?) {
                uiActions.showErrorAlert()
            }

            override fun onResponse(call: Call<BackupApi.StatusResponse>?, response: Response<BackupApi.StatusResponse>?) {
                response?.let {
                    if (it.isSuccessful) {
                        userRepository.isBackedup = true
                        onMoveNextStep()
                        analytics.logEvent(Events.Business.WalletBackedUp())
                        Log.d("BackupModel", "send list of question ids $list")
                    } else {
                        uiActions.showErrorAlert()
                    }
                }
            }
        })
    }

    private fun reset() {
        isClickable.set(true)
        currentAnswer = null
        currentQuestion = null
        step = 0
        questionsAndAnswers.clear()
        encryptedAccountStr = null
    }

    private fun saveQuestionAnswer() {
        currentQuestion?.let { question ->
            currentAnswer?.let { answer ->
                questionsAndAnswers.add(question to answer)
            }
        }
        currentQuestion = null
        currentAnswer = null
        isQuestionSelected.set(false)
        isAnswerValid.set(false)
        isNextEnabled.set(false)
    }

    private fun onQuestionSelected(question: BackupApi.BackUpQuestion) {
        if (question.isValid()) {
            isQuestionSelected.set(true)
            currentQuestion = question
        } else {
            currentAnswer = null
            isAnswerValid.set(false)
            isQuestionSelected.set(false)
        }
        isNextEnabled.set(isQuestionSelected.get() && isAnswerValid.get())
    }
}

fun BackupApi.BackUpQuestion.isValid() = id != INVALID_HINT_ID