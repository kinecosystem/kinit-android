package org.kinecosystem.kinit.viewmodel.bootwallet

import android.databinding.ObservableBoolean
import kin.sdk.migration.common.interfaces.IKinClient
import kin.sdk.migration.common.interfaces.IMigrationManagerCallbacks
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.blockchain.Wallet
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.server.OnboardingService
import org.kinecosystem.kinit.server.OperationCompletionCallback
import org.kinecosystem.kinit.server.api.BackupApi
import java.lang.Exception
import javax.inject.Inject

class RestoreWalletViewModel {
    @Inject
    lateinit var onboardingService: OnboardingService
    @Inject
    lateinit var walletService: Wallet
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var analytics: Analytics


    lateinit var qrCode: String
    var answers: Array<String>
    var passphrase: String = ""
    var step = 0
    val answersSubmitted: ObservableBoolean = ObservableBoolean(false)
    val nextEnabled: ObservableBoolean = ObservableBoolean(false)
    var listener: RestoreWalletViewModelListener? = null

    init {
        KinitApplication.coreComponent.inject(this)
        answers = Array(userRepository.restoreHints.size) { "" }
    }

    interface RestoreWalletViewModelListener {
        fun onError(msg: RestoreWalletActivityMessages?)
        fun onSuccess()
    }

    fun onAnswersSubmit() {
        Events.Analytics.ClickConfirmButtonOnAnswerSecurityQuestionsPage()
        answersSubmitted.set(true)
        passphrase = answers.joinToString(separator = "") { it.trim() }
        var restoredAccount = walletService.importBackedUpAccount(qrCode, passphrase)
        if (restoredAccount == null || restoredAccount.publicAddress == null) {
            passphrase = answers.joinToString(separator = "")
            restoredAccount = walletService.importBackedUpAccount(qrCode, passphrase)
        }
        if (restoredAccount == null || restoredAccount.publicAddress == null) {
            answersSubmitted.set(false)
            listener?.onError(RestoreWalletActivityMessages.RESTORE_ERROR)
        } else {
            onboardingService.restoreAccount(restoredAccount.publicAddress.orEmpty(), object : OperationCompletionCallback {
                override fun onSuccess() {
                    walletService.migrateWallet(object: IMigrationManagerCallbacks {
                        override fun onReady(kinClient: IKinClient?) {
                            analytics.logEvent(Events.Business.MigrationSucceeded())
                            listener?.onSuccess()
                        }

                        override fun onMigrationStart() {
                            analytics.logEvent(Events.Business.MigrationStarted())
                        }

                        override fun onError(e: Exception?) {
                            analytics.logEvent(Events.BILog.MigrationFailed(e?.message))
                            listener?.onError(RestoreWalletActivityMessages.RESTORE_SERVER_ERROR)
                            answersSubmitted.set(false)
                        }
                    })
                }

                override fun onError(errorCode: Int) {
                    answersSubmitted.set(false)
                    listener?.onError(RestoreWalletActivityMessages.RESTORE_SERVER_ERROR)
                }
            })
        }
    }

    fun onBack() {
        step--
    }

    fun onNext() {
        step++
    }

    fun getState(): RestoreState {
        return when (step) {
            0 -> RestoreState.Welcomeback
            1 -> RestoreState.Intro
            2 -> RestoreState.QrScan
            3 -> RestoreState.SecurityQuestions
            4 -> RestoreState.Complete
            else -> {
                restart()
                RestoreState.Welcomeback
            }
        }
    }

    private fun restart() {
        step = 0
        answersSubmitted.set(false)
    }

    fun onCodeReceived(qrCode: String) {
        this.qrCode = qrCode
    }

    fun setAnswer(index: Int, str: String) {
        answers[index] = str
        if (answers.none { answer -> answer.length < 4 })
            nextEnabled.set(true)
        else
            nextEnabled.set(false)
    }

    fun getHintQuestionById(id: Int): BackupApi.BackUpQuestion {
        return userRepository.backUpHints.filter { it.id == id }[0]
    }

    fun onResume() {
        when (getState()) {
            RestoreState.Welcomeback -> analytics.logEvent(Events.Analytics.ViewWelcomeBackPage())
            RestoreState.Intro -> analytics.logEvent(Events.Analytics.ViewScanPage())
            RestoreState.SecurityQuestions -> analytics.logEvent(Events.Analytics.ViewAnswerSecurityQuestionsPage())
        }
    }
}

enum class RestoreState {
    Welcomeback, Intro, QrScan, SecurityQuestions, Complete
}

enum class RestoreWalletActivityMessages {
    CREATE_WALLET_WARNING,
    QR_ERROR,
    NETWORK_ERROR,
    RESTORE_ERROR,
    RESTORE_SERVER_ERROR
}