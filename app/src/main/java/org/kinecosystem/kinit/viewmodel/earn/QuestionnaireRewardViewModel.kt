package org.kinecosystem.kinit.viewmodel.earn

import android.databinding.ObservableBoolean
import org.kinecosystem.kinit.CoreComponentsProvider
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.TaskState
import org.kinecosystem.kinit.model.earn.tagsString
import org.kinecosystem.kinit.view.earn.TransactionTimeout

private const val REWARD_TIMEOUT: Long = 20000

class QuestionnaireRewardViewModel(val coreComponents: CoreComponentsProvider,
    private var timeoutCallback: TransactionTimeout? = null) {

    private val analytics = coreComponents.analytics()
    private val repo = coreComponents.questionnaireRepo()
    val task = repo.task
    private val walletService = coreComponents.services().walletService
    val balance = walletService.balance
    val onTransactionComplete: ObservableBoolean =
        if (repo.taskState == TaskState.TRANSACTION_COMPLETED) {
            ObservableBoolean(true)
        } else {
            waitForReward()
            walletService.onEarnTransactionCompleted
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
        coreComponents.scheduler().scheduleOnMain(
            {
                if (!onTransactionComplete.get()) {
                    timeoutCallback?.onTransactionTimeout()
                    // update balance anyway in case kin has been received
                    coreComponents.services().walletService.updateBalance()
                    walletService.retrieveTransactions()
                }
            },
            REWARD_TIMEOUT)
    }

}
