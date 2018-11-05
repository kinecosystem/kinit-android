package org.kinecosystem.kinit.view.createWallet

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.databinding.CreateWalletErrorFragmentBinding
import org.kinecosystem.kinit.view.BaseFragment
import javax.inject.Inject

class CreateWalletErrorFragment : BaseFragment() {
    @Inject
    lateinit var analytics: Analytics

    companion object {
        val TAG = CreateWalletErrorFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(): CreateWalletErrorFragment {
            return CreateWalletErrorFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<CreateWalletErrorFragmentBinding>(
                inflater, R.layout.create_wallet_error_fragment, container, false)
        try {
            binding.model = (activity as CreateWalletActivity).getModel()
        } catch (e: Exception) {
            Log.e(CreateWalletFragment.TAG, "Invalid data cant start CreateWalletErrorFragment")
            activity?.finish()
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        KinitApplication.coreComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        analytics.logEvent(Events.Analytics.ViewErrorPage(Analytics.VIEW_ERROR_TYPE_ONBOARDING, "create wallet error"))
    }
}