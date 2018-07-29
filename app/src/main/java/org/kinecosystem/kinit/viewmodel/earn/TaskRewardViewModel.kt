package org.kinecosystem.kinit.viewmodel.earn

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.TaskState
import org.kinecosystem.kinit.model.earn.Task
import org.kinecosystem.kinit.model.earn.tagsString
import org.kinecosystem.kinit.network.ServicesProvider
import org.kinecosystem.kinit.network.Wallet
import org.kinecosystem.kinit.repository.TasksRepository
import org.kinecosystem.kinit.util.Scheduler
import org.kinecosystem.kinit.view.earn.TransactionTimeout
import javax.inject.Inject

const val REWARD_TIMEOUT: Long = 30000

class TaskRewardViewModel(private var timeoutCallback: TransactionTimeout? = null) {

    @Inject
    lateinit var scheduler: Scheduler
    @Inject
    lateinit var servicesProvider: ServicesProvider
    @Inject
    lateinit var taskRepository: TasksRepository
    @Inject
    lateinit var analytics: Analytics
    var task: Task?
    private var walletService: Wallet
    var balance: ObservableField<String>
    var onTransactionComplete: ObservableBoolean

    private var submitFailed: Boolean = false
        get() = (taskRepository.taskState == TaskState.SUBMITTED || taskRepository.taskState == TaskState.SUBMIT_ERROR_RETRY || taskRepository.taskState == TaskState.SUBMIT_ERROR_NO_RETRY)

    init {
        KinitApplication.coreComponent.inject(this)
        task = taskRepository.task
        walletService = servicesProvider.walletService
        balance = walletService.balance
        onTransactionComplete =
                if (taskRepository.taskState == TaskState.TRANSACTION_COMPLETED) {
                    ObservableBoolean(true)
                } else {
                    waitForReward()
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


    private fun waitForReward() {
        scheduler.scheduleOnMain(
                {
                    if (!onTransactionComplete.get()) {
                        if (submitFailed) {
                            timeoutCallback?.onSubmitError()
                        } else {
                            timeoutCallback?.onTransactionTimeout()
                        }
                        // update balance anyway in case kin has been received
                        servicesProvider.walletService.updateBalance()
                        walletService.retrieveTransactions()
                    }
                },
                REWARD_TIMEOUT)
    }

}
