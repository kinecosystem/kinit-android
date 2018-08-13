package org.kinecosystem.kinit.viewmodel.Backup

import android.databinding.ObservableBoolean
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.network.BackupApi
import org.kinecosystem.kinit.network.ServicesProvider
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.util.GeneralUtils
import org.kinecosystem.kinit.util.isValidEmail
import javax.inject.Inject

const val INVALID_HINT_ID = -1
const val QUESTIONS_COUNT = 2

class BackUpModel : AdapterView.OnItemSelectedListener, TextWatcher {


    enum class BackupState {
        Welcome, Question, Summery, QRCode, Confirm, Complete
    }

    @Inject
    lateinit var servicesProvider: ServicesProvider

    @Inject
    lateinit var userRepository: UserRepository
    var isQuestionSelected = ObservableBoolean(false)
    var isNextEnabled = ObservableBoolean(false)
    var titles: Array<String> = listOf("Next Question").toTypedArray()
    private var isAnswerValid = ObservableBoolean(false)
    private var currentAnswer: String? = null
    private var email: String? = null
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
                        it.toString()
                    } else {
                        isAnswerValid.set(false)
                        null
                    }
                    isNextEnabled.set(isQuestionSelected.get() && isAnswerValid.get())
                }
                BackupState.QRCode -> {
                    email = if (it.isValidEmail()) {
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

    fun initHints() {
        servicesProvider.backupService.initHints()
    }

    fun onNext() {
        onStateComplete()
        step++
    }

    fun getTitle() = when (step - 1) {
        in 0 until titles.size -> titles[step - 1]
        else -> "Next Question"
    }

    fun getState(): BackupState {
        return when (step) {
            in 1..QUESTIONS_COUNT -> BackupState.Question
            QUESTIONS_COUNT + 1 -> BackupState.Summery
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

    private fun onStateComplete() {
        when (getState()) {
            BackupState.Question -> {
                saveQAandReset()
            }
            BackupState.Summery -> {
                sendChosenQuestions()
            }
            BackupState.QRCode -> {
                sendEmail()
            }
            BackupState.Confirm -> {
                userRepository.isBackedup = true
            }
        }
    }

    private fun sendEmail() {
        email?.let {

            //TODO do in the background if get key takes time
            servicesProvider.backupService.updateBackupDataTo(it, getEncryptedKey())
            Log.d("BackUpModel", "#### send email to $it")
            email = null
            isNextEnabled.set(false)
        }
    }

    private fun getEncryptedKey(): String {
        //TODO get from wallet
        return "Mary Had a Little Lamb"
    }

    private fun sendChosenQuestions() {
        var list: MutableList<Int> = mutableListOf()
        questionsAndAnswers.forEach {
            list.add(it.first.id)
        }
        servicesProvider.backupService.updateHints(list)
        Log.d("BackUpModel", "#### BackUpModel send list of question ids $list")
    }

    private fun reset() {
        currentAnswer = null
        currentQuestion = null
        step = 0
        questionsAndAnswers.clear()
    }

    private fun saveQAandReset() {
        if (currentAnswer != null && currentQuestion != null) {
            questionsAndAnswers.add(currentQuestion!! to currentAnswer!!)
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