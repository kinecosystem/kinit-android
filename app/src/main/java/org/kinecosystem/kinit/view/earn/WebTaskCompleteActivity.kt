package org.kinecosystem.kinit.view.earn

import android.content.Context
import android.content.Intent
import org.kinecosystem.kinit.view.SingleFragmentActivity
import org.kinecosystem.kinit.view.earn.TaskErrorFragment.ERROR_TRANSACTION

class WebTaskCompleteActivity : SingleFragmentActivity(), TaskWebCompleteFragment.TaskCompleteListener, TransactionActions {
    override fun transactionError() {
        replaceFragment(TaskErrorFragment.newInstance(ERROR_TRANSACTION))
    }

    val taskWebCompleteFragment: TaskWebCompleteFragment = TaskWebCompleteFragment.newInstance()

    override fun onAnimationComplete() {
        replaceFragment(QuestionnaireRewardFragment.newInstance())
    }


    companion object {
        fun getIntent(context: Context) = Intent(context, WebTaskCompleteActivity::class.java)
    }

    init {
        taskWebCompleteFragment.listener = this
    }

    override fun getFragment() = taskWebCompleteFragment
}