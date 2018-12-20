package org.kinecosystem.kinit.view.transfer

import android.databinding.DataBindingUtil
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.TransferringKinToAppLayoutBinding
import org.kinecosystem.kinit.model.spend.EcoApplication
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.util.GeneralUtils
import org.kinecosystem.kinit.view.BaseFragment
import org.kinecosystem.kinit.view.spend.AppDetailFragment
import org.kinecosystem.kinit.viewmodel.spend.TransferringToAppViewModel

class TransferringFragment : BaseFragment() {

    lateinit var model: TransferringToAppViewModel
    var transferActions: TransferActions? = null
    lateinit var binding: TransferringKinToAppLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<TransferringKinToAppLayoutBinding>(inflater, R.layout.transferring_kin_to_app_layout, container, false)

        if (activity is TransferActions) {
            transferActions = activity as TransferActions
        } else {
            activity?.finish()
        }
        arguments?.let { args ->
            if (args.containsKey(ARG_APP)) {
                val app =  args.getParcelable<EcoApplication>(ARG_APP)
                val amount = args.getInt(ARG_AMOUNT)
                context?.let {
                    model = TransferringToAppViewModel(Navigator(it), app, amount, transferActions)
                    binding.model = model
                }
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        model.onResume()
        GeneralUtils.closeKeyboard(context, binding.balanceSubtitle)
    }

    override fun onPause() {
        super.onPause()
        model.onPause()
    }

    companion object {
        val ARG_APP = "arg_app"
        val ARG_AMOUNT = "arg_amount"

        fun newInstance(app: EcoApplication, amount: Int): TransferringFragment {
            val fragment = TransferringFragment()
            val args = Bundle()
            args.putParcelable(ARG_APP, app)
            args.putInt(ARG_AMOUNT, amount)
            fragment.arguments = args
            return fragment
        }
    }


}
