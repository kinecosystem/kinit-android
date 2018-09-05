package org.kinecosystem.kinit.view.backup

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.backup_qrcode.*
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.databinding.BackupQrcodeBinding
import org.kinecosystem.kinit.util.GeneralUtils
import org.kinecosystem.kinit.view.BaseFragment
import org.kinecosystem.kinit.view.customView.AlertManager
import javax.inject.Inject


class BackupQRCodeFragment : BaseFragment() {
    companion object {
        fun newInstance(): Fragment = BackupQRCodeFragment()
    }

    @Inject
    lateinit var analytics: Analytics

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        next.setOnClickListener {
            activity?.let {
                GeneralUtils.closeKeyboard(activity, next)
                if (GeneralUtils.isConnected(activity)) {
                    (it as BackupActions).getBackUpModel().onNext()
                } else {
                    AlertManager.showAlertNoIternetDismiss(it)
                }
            }
        }

        backBtn.setOnClickListener({
            activity?.let {
                (it as BackupActions).onBack()
            }
        })
        analytics.protectView(userInput)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (activity !is BackupActions) {
            Log.e("BackupQRCodeFragment", "Activity must implement BackupActions")
            activity?.finish()
        }
        val binding = DataBindingUtil.inflate<BackupQrcodeBinding>(inflater, R.layout.backup_qrcode, container, false)
        binding.model = (activity as BackupActions).getBackUpModel()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        KinitApplication.coreComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        activity?.let {
            (it as BackupActions).getBackUpModel().onResumeFragment()
        }
    }
}