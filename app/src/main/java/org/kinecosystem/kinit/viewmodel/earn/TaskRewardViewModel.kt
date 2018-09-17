package org.kinecosystem.kinit.viewmodel.earn

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.util.Log
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.TaskState
import org.kinecosystem.kinit.model.earn.Task
import org.kinecosystem.kinit.model.earn.tagsString
import org.kinecosystem.kinit.server.ServicesProvider
import org.kinecosystem.kinit.blockchain.Wallet
import org.kinecosystem.kinit.repository.TasksRepository
import org.kinecosystem.kinit.server.TaskService
import org.kinecosystem.kinit.util.Scheduler
import org.kinecosystem.kinit.view.earn.TransactionTimeout
import javax.inject.Inject

const val REWARD_TIMEOUT: Long = 30000
const val EXTENDED_REWARD_TIMEOUT: Long = 40000

class TaskRewardViewModel(private var timeoutCallback: TransactionTimeout? = null) {

    @Inject
    lateinit var scheduler: Scheduler
    @Inject
    lateinit var taskRepository: TasksRepository
    @Inject
    lateinit var analytics: Analytics
    @Inject
    lateinit var walletService: Wallet
    @Inject
    lateinit var taskService: TaskService

    var task: Task?
    var balance: ObservableField<String>
    var onTransactionComplete: ObservableBoolean

    private var submitFailed: Boolean = false
        get() = (taskRepository.taskState == TaskState.SUBMITTED || taskRepository.taskState == TaskState.SUBMIT_ERROR_RETRY || taskRepository.taskState == TaskState.SUBMIT_ERROR_NO_RETRY)

    init {
        KinitApplication.coreComponent.inject(this)
        task = taskRepository.task
        balance = walletService.balance
        onTransactionComplete =
                if (taskRepository.taskState == TaskState.TRANSACTION_COMPLETED) {
                    ObservableBoolean(true)
                } else {
                    waitForReward(if (taskRepository.taskState == TaskState.SHOWING_CAPTCHA) EXTENDED_REWARD_TIMEOUT else REWARD_TIMEOUT)
                    walletService.onEarnTransactionCompleted
                }
    }

    fun onResume() {
        rewardPageShown()
    }

    fun detach() {
        timeoutCallback = null
    }

    fun attach(callback: TransactionTimeout) {
        timeoutCallback = callback
    }

    private fun rewardPageShown() {
        val event = Events.Analytics.ViewRewardPage(
                task?.provider?.name,
                task?.minToComplete,
                task?.kinReward,
                task?.tagsString(),
                task?.id,
                task?.title,
                task?.type)
        analytics.logEvent(event)
    }


    private fun waitForReward(timeout: Long) {
        scheduler.scheduleOnMain(
                {
                    if (!onTransactionComplete.get()) {
                        val timeDiff = System.currentTimeMillis()  - taskService.lastSubmissionTime
                        if (submitFailed) {
                            timeoutCallback?.onSubmitError()
                        } else if (taskRepository.taskState == TaskState.SHOWING_CAPTCHA) {
                            waitForReward(REWARD_TIMEOUT)
                        }
                        else if (timeDiff < REWARD_TIMEOUT) {
                            // wait some more
                            waitForReward(REWARD_TIMEOUT-timeDiff + 2000)
                        }
                        else {
                            timeoutCallback?.onTransactionTimeout()
                        }
                        // update balance anyway in case kin has been received
                        walletService.updateBalance()
                        walletService.retrieveTransactions()
                    }
                },
                timeout)
    }

}
