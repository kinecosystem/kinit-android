package org.kinecosystem.tippic.viewmodel.earn

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.util.Log
import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.analytics.Analytics
import org.kinecosystem.tippic.analytics.Events
import org.kinecosystem.tippic.blockchain.Wallet
import org.kinecosystem.tippic.model.TaskState
import org.kinecosystem.tippic.repository.CategoriesRepository
import org.kinecosystem.tippic.server.TaskService
import org.kinecosystem.tippic.util.Scheduler
import org.kinecosystem.tippic.view.earn.TransactionTimeout
import javax.inject.Inject

const val REWARD_TIMEOUT: Long = 30000
const val EXTENDED_REWARD_TIMEOUT: Long = 40000
const val SMALL_DELAY: Long = 2000
const val TAG = "TaskRewardViewModel"

class TaskRewardViewModel(private var timeoutCallback: TransactionTimeout? = null) {

    @Inject
    lateinit var scheduler: Scheduler
    @Inject
    lateinit var categoriesRepository: CategoriesRepository
    @Inject
    lateinit var analytics: Analytics
    @Inject
    lateinit var walletService: Wallet
    @Inject
    lateinit var taskService: TaskService

    var balance: ObservableField<String>
    var onTransactionComplete: ObservableBoolean

    private var submitFailed: Boolean = false
        get() = (categoriesRepository.getCurrentTaskState() == TaskState.SUBMIT_ERROR_RETRY || categoriesRepository.getCurrentTaskState() == TaskState.SUBMIT_ERROR_NO_RETRY)

    private var submitted: Boolean = false
        get() = (categoriesRepository.getCurrentTaskState() == TaskState.SUBMITTED)

    init {
        TippicApplication.coreComponent.inject(this)
        balance = walletService.balance
        onTransactionComplete =
                if (categoriesRepository.getCurrentTaskState() == TaskState.TRANSACTION_COMPLETED) {
                    Log.d(TAG, "taskState - transaction already completed ")
                    ObservableBoolean(true)
                } else {
                    walletService.onEarnTransactionCompleted
                }
    }

    fun onResume() {
        rewardPageShown()
        if (submitFailed) {
            scheduler.scheduleOnMain(
                    {
                        timeoutCallback?.onSubmitError()
                    }, SMALL_DELAY)
        } else if (!onTransactionComplete.get()) {
            val timeout = if (categoriesRepository.getCurrentTaskState() == TaskState.SHOWING_CAPTCHA) EXTENDED_REWARD_TIMEOUT else REWARD_TIMEOUT
            Log.d(TAG, "waiting for reward $timeout")
            waitForReward(timeout)
        }
    }

    fun detach() {
        timeoutCallback = null
    }

    fun attach(callback: TransactionTimeout) {
        timeoutCallback = callback
    }

    private fun rewardPageShown() {
        val task = categoriesRepository.currentTaskInProgress
        val event = Events.Analytics.ViewRewardPage(
                task?.provider?.name,
                task?.minToComplete,
                task?.kinReward,
                categoriesRepository.currentCategoryTitle,
                task?.id,
                task?.title,
                task?.type)
        analytics.logEvent(event)
    }


    private fun waitForReward(timeout: Long) {
        scheduler.scheduleOnMain(
                {
                    if (!onTransactionComplete.get()) {
                        val timeDiff = System.currentTimeMillis() - taskService.lastSubmissionTime
                        if (submitted || submitFailed) {
                            Log.d(TAG, "timeout reached with taskState= ${categoriesRepository.getCurrentTaskState()}")
                            onTimeoutCompleted()
                            timeoutCallback?.onSubmitError()
                        } else if (categoriesRepository.getCurrentTaskState() == TaskState.SHOWING_CAPTCHA) {
                            Log.d(TAG,
                                    "timeout reached however state is SHOWING_CAPTCHA will wait some more. taskState= ${categoriesRepository.getCurrentTaskState()}")
                            waitForReward(REWARD_TIMEOUT)
                        } else if (timeDiff < REWARD_TIMEOUT) {
                            Log.d(TAG,
                                    "timeout reached however timeDiff since submission is $timeDiff < $REWARD_TIMEOUT. will wait some more. taskState= ${categoriesRepository.getCurrentTaskState()}")
                            // wait some more
                            waitForReward(REWARD_TIMEOUT - timeDiff + SMALL_DELAY)
                        } else {
                            onTimeoutCompleted()
                            Log.d(TAG, "timeout reached transaction error")
                            timeoutCallback?.onTransactionTimeout()
                        }
                    }
                },
                timeout)
    }

    private fun onTimeoutCompleted() {
        // when timeout is reached, update balance anyway in case kin has been received
        walletService.updateBalance()
        walletService.retrieveTransactions()
    }

}
