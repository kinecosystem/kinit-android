package org.kinecosystem.kinit.view.backup

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.backup_welcome_layout.*
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.util.GeneralUtils
import org.kinecosystem.kinit.view.customView.AlertManager

class WelcomeBackupFragment : Fragment() {

    companion object {
        fun newInstance(): Fragment = WelcomeBackupFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        closeBtn.setOnClickListener { activity?.finish() }
        backupBtn.setOnClickListener {
            activity?.let {
                if (GeneralUtils.isConnected(it)) {
                    (it as BackupActions).getBackUpModel().onNext()
                } else {
                    AlertManager.showAlert(it, R.string.no_internet_connection, R.string.no_internet_message, R.string.back, {
                        it.finish()
                    })
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (activity !is BackupActions) {
            Log.e("WelcomeBackupFragment", "Activity must implement BackupActions")
            activity?.finish()
        }
        return inflater.inflate(R.layout.backup_welcome_layout, container, false)
    }
}