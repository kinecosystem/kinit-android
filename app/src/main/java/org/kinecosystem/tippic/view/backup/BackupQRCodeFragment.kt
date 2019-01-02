package org.kinecosystem.tippic.view.backup

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.backup_qrcode.*
import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.R
import org.kinecosystem.tippic.analytics.Analytics
import org.kinecosystem.tippic.databinding.BackupQrcodeBinding
import org.kinecosystem.tippic.util.GeneralUtils
import org.kinecosystem.tippic.view.BaseFragment
import org.kinecosystem.tippic.view.customView.AlertManager
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
        TippicApplication.coreComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        activity?.let {
            (it as BackupActions).getBackUpModel().onResumeFragment()
        }
    }
}