package org.kinecosystem.kinit.view.backup

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import org.kinecosystem.kinit.view.SingleFragmentActivity
import org.kinecosystem.kinit.view.tutorial.TutorialEarnFragment

class WalletBackupActivity : SingleFragmentActivity() , BackUpActions {
    override fun getFragment(): Fragment {
        return WelcomeBackupFragment.newInstance()
    }

    override fun onStartBackup() {
        replaceFragment(TutorialEarnFragment.newInstance(), true)
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, WalletBackupActivity::class.java)
    }

}

interface BackUpActions{
    fun onStartBackup()
}