package org.kinecosystem.kinit.view.backup

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.welcome_wallet_backup.*
import org.kinecosystem.kinit.R

class WelcomeBackupFragment : Fragment() {

    companion object {
        fun newInstance(): Fragment = WelcomeBackupFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity !is BackUpActions) {
            Log.e("WelcomeBackupFragment", "Activity must implement BackUpActions")
            activity?.finish()
        }
        closeBtn.setOnClickListener { activity?.finish() }
        backupBtn.setOnClickListener {
            activity?.let {
                (it as BackUpActions).onStartBackup()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.welcome_wallet_backup, container, false)
    }
}