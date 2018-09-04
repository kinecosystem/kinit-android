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
import org.kinecosystem.kinit.view.BaseFragment


class BackupConfirmFragment : BaseFragment() {
    companion object {
        fun newInstance(): Fragment = BackupConfirmFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subtitle.text = Html.fromHtml(resources.getString(R.string.check_email))
        subtitle.setOnClickListener({ view ->
            activity?.let {
                (it as BackupActions).onBack()
            }
        })

        next.setOnClickListener {
            activity?.let {
                (it as BackupActions).getBackUpModel().onNext()
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
            Log.e("BackupConfirmFragment", "Activity must implement BackupActions")
            activity?.finish()
        }
        val binding = DataBindingUtil.inflate<BackupConfirmBinding>(inflater, R.layout.backup_confirm, container, false)
        binding.model = (activity as BackupActions).getBackUpModel()
        return binding.root
    }
}