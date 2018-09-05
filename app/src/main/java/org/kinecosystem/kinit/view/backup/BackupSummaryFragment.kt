package org.kinecosystem.kinit.view.backup

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.backup_summary.*
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.BackupSummaryBinding
import org.kinecosystem.kinit.util.GeneralUtils
import org.kinecosystem.kinit.view.BaseFragment
import org.kinecosystem.kinit.view.customView.AlertManager


class BackupSummaryFragment : BaseFragment() {
    companion object {
        fun newInstance(): Fragment = BackupSummaryFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        next.setOnClickListener {
            activity?.let {
                if (GeneralUtils.isConnected(it)) {
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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (activity !is BackupActions) {
            Log.e("BackupSummaryFragment", "Activity must implement BackupActions")
            activity?.finish()
        }
        val binding = DataBindingUtil.inflate<BackupSummaryBinding>(inflater, R.layout.backup_summary, container, false)
        binding.model = (activity as BackupActions).getBackUpModel()
        return binding.root
    }
}