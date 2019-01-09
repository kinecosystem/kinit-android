package org.kinecosystem.kinit.view.transfer

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.AppsConnectionsLayoutBinding
import org.kinecosystem.kinit.model.spend.EcoApplication
import org.kinecosystem.kinit.view.BaseFragment
import org.kinecosystem.kinit.view.spend.AppDetailFragment
import org.kinecosystem.kinit.viewmodel.spend.AppsConnectionViewModel

class AppsConnectionFragment : BaseFragment() {

    lateinit var model: AppsConnectionViewModel
    var transferActions: TransferActions? = null
    lateinit var binding: AppsConnectionsLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<AppsConnectionsLayoutBinding>(inflater, R.layout.apps_connections_layout, container, false)

        if (activity is TransferActions) {
            transferActions = activity as TransferActions
        } else {
            activity?.finish()
        }
        arguments?.let { args ->
            if (args.containsKey(AppDetailFragment.ARG_APP)) {
                val app = args.getParcelable<EcoApplication>(AppsConnectionFragment.ARG_APP)
                context?.let {
                    model = AppsConnectionViewModel(app)
                    binding.model = model
                }
            }
        }
        return binding.root
    }

    companion object {
        val ARG_APP = "arg_app"

        fun newInstance(app: EcoApplication): AppsConnectionFragment {
            val fragment = AppsConnectionFragment()
            val args = Bundle()
            args.putParcelable(ARG_APP, app)
            fragment.arguments = args
            return fragment
        }
    }


}
