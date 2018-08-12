package org.kinecosystem.kinit.view.earn

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.webkit.WebViewFragment
import org.kinecosystem.kinit.view.SingleFragmentActivity
import org.kinecosystem.kinit.view.earn.TaskErrorFragment.ERROR_TRANSACTION

class WebTaskCompleteActivity : SingleFragmentActivity(), WebTaskCompleteFragment.TaskCompleteListener,
    TransactionActions {
    override fun getFragment(): Fragment {
        val fragment = WebTaskCompleteFragment.newInstance()
        fragment.listener = this
        return fragment
    }

    override fun transactionError() {
        replaceFragment(TaskErrorFragment.newInstance(ERROR_TRANSACTION))
    }

    override fun onAnimationComplete() {
        replaceFragment(TaskRewardFragment.newInstance())
    }


    companion object {
        fun getIntent(context: Context) = Intent(context, WebTaskCompleteActivity::class.java)
    }
}