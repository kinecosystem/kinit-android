package org.kinecosystem.kinit.view.bootWallet

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
import org.kinecosystem.kinit.databinding.BootWalletFragmentBinding
import org.kinecosystem.kinit.view.BaseFragment
import javax.inject.Inject

class BootWalletFragment : BaseFragment() {
    @Inject
    lateinit var analytics: Analytics

    companion object {
        val TAG = BootWalletFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(): BootWalletFragment {
            return BootWalletFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<BootWalletFragmentBinding>(
                inflater, R.layout.boot_wallet_fragment, container, false)
        try {
            binding.model = (activity as BootWalletActivity).model
        } catch (e: Exception) {
            Log.e(TAG, "Invalid data cant start BootWalletErrorFragment")
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
        analytics.logEvent(Events.Analytics.ViewCreatingWalletPage())
    }
}