package org.kinecosystem.kinit.view.transfer

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.TransferKinToAppLayoutBinding
import org.kinecosystem.kinit.model.spend.EcosystemApp
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.util.GeneralUtils
import org.kinecosystem.kinit.view.BaseFragment
import org.kinecosystem.kinit.view.spend.AppDetailFragment
import org.kinecosystem.kinit.viewmodel.spend.TransferToAppViewModel


class SendAmountFragment : BaseFragment() {

    var model: TransferToAppViewModel? = null
    var transferActions: TransferActions? = null
    lateinit var binding: TransferKinToAppLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<TransferKinToAppLayoutBinding>(inflater, R.layout.transfer_kin_to_app_layout, container, false)

        transferActions = activity as TransferActions
        arguments?.let { args ->
            if (args.containsKey(AppDetailFragment.ARG_APP)) {
                val app = args.getParcelable<EcosystemApp>(SendAmountFragment.ARG_APP)
                context?.let {
                    model = TransferToAppViewModel(Navigator(it), app, transferActions)
                    binding.model = model
                }
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val handler = Handler()
        handler.postDelayed({
            binding.amount.requestFocus()
            GeneralUtils.openKeyboard(context, binding.amount)
        }, 750)
    }

    override fun onPause() {
        super.onPause()
        binding.amount.clearFocus()
        GeneralUtils.closeKeyboard(context, binding.amount)

    }

    override fun onDetach() {
        super.onDetach()
        model?.onDetach()
        transferActions = null
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        model?.transferActions = activity as TransferActions
    }

    companion object {
        val ARG_APP = "arg_app"
        val TAG: String = SendAmountFragment::class.java.simpleName
        fun newInstance(app: EcosystemApp): SendAmountFragment {
            val fragment = SendAmountFragment()
            val args = Bundle()
            args.putParcelable(ARG_APP, app)
            fragment.arguments = args
            return fragment
        }
    }


}
