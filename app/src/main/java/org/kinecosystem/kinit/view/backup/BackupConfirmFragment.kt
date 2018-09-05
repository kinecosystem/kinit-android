package org.kinecosystem.kinit.view.backup

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.backup_confirm.*
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.BackupConfirmBinding
import org.kinecosystem.kinit.util.GeneralUtils
import org.kinecosystem.kinit.view.BaseFragment
import org.kinecosystem.kinit.view.customView.AlertManager


class BackupConfirmFragment : BaseFragment() {
    companion object {
        fun newInstance(): Fragment = BackupConfirmFragment()
    }

    lateinit var model:BackupActions

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var seenBackAlert = false
        subtitle.text = Html.fromHtml(resources.getString(R.string.check_email))
        subtitle.setOnClickListener {
            activity?.let {
                if ((activity as BackupActions).shouldShowAlertOnResendClick()) {
                    AlertManager.showAlert(it, R.string.huh_that_doesn_t_seem_right, R.string.problem_email_confirmation, R.string.dialog_ok, {
                        it.finish()
                    })
                } else {
                    (it as BackupActions).onBack()
                }
            }
        }

        next.setOnClickListener {
            activity?.let {
                if (GeneralUtils.isConnected(it)) {
                    (it as BackupActions).getBackUpModel().onNext()
                } else {
                    AlertManager.showAlertNoIternetDismiss(it)
                }
            }
        }

        backBtn.setOnClickListener {
            activity?.let {
                if (seenBackAlert) {
                    (it as BackupActions).onBack()
                } else {
                    seenBackAlert = true
                    AlertManager.showAlert(it, R.string.cancel_backup_title, R.string.cancel_backup_message, R.string.cancel_backup, {
                        it.finish()
                    }, R.string.continue_backup, {})
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (activity !is BackupActions) {
            Log.e("BackupConfirmFragment", "Activity must implement BackupActions")
            activity?.finish()
        }
        val binding = DataBindingUtil.inflate<BackupConfirmBinding>(inflater, R.layout.backup_confirm, container, false)
        binding.model = (activity as BackupActions).getBackUpModel()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        activity?.let {
            (it as BackupActions).getBackUpModel().onResumeFragment()
        }
    }
}