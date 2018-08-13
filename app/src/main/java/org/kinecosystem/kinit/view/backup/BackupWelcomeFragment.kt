package org.kinecosystem.kinit.view.backup

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.backup_welcome_layout.*
import org.kinecosystem.kinit.R

class WelcomeBackupFragment : Fragment() {

    companion object {
        fun newInstance(): Fragment = WelcomeBackupFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        closeBtn.setOnClickListener { activity?.finish() }
        backupBtn.setOnClickListener {
            activity?.let {
                (it as BackUpActions).onNext()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (activity !is BackUpActions) {
            Log.e("WelcomeBackupFragment", "Activity must implement BackUpActions")
            activity?.finish()
        }
        return inflater.inflate(R.layout.backup_welcome_layout, container, false)
    }
}