package org.kinecosystem.kinit.view.earn

import android.content.Context
import android.content.Intent
import org.kinecosystem.kinit.view.SingleFragmentActivity

class CompleteWebTaskActivity : SingleFragmentActivity(), TaskWebCompleteFragment.TaskCompleteListener {
    val taskWebCompleteFragment: TaskWebCompleteFragment = TaskWebCompleteFragment.newInstance()

    override fun onAnimationComplete() {
        replaceFragment(QuestionnaireRewardFragment.newInstance())
    }


    companion object {
        fun getIntent(context: Context) = Intent(context, CompleteWebTaskActivity::class.java)
    }

    init {
        taskWebCompleteFragment.listener = this
    }

    override fun getFragment() = taskWebCompleteFragment
}