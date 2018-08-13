package org.kinecosystem.kinit.view.backup

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.backup_summery.*
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.BackupSummeryBinding
import org.kinecosystem.kinit.view.BaseFragment


class BackupSummeryFragment : BaseFragment() {
    companion object {
        fun newInstance(): Fragment = BackupSummeryFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        next.setOnClickListener {
            activity?.let {
                (it as BackUpActions).onNext()
            }
        }

        backBtn.setOnClickListener({
            activity?.let {
                (it as BackUpActions).onBack()
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (activity !is BackUpActions) {
            Log.e("BackupSummeryFragment", "Activity must implement BackUpActions")
            activity?.finish()
        }
        val binding = DataBindingUtil.inflate<BackupSummeryBinding>(inflater, R.layout.backup_summery, container, false)
        binding.model = (activity as BackUpActions).getBackUpModel()
        return binding.root
    }
}