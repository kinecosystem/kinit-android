package org.kinecosystem.kinit.view.migrateWallet

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
import org.kinecosystem.kinit.databinding.MigrateWalletErrorFragmentBinding
import org.kinecosystem.kinit.view.BaseFragment
import javax.inject.Inject

class MigrateWalletErrorFragment : BaseFragment() {
    @Inject
    lateinit var analytics: Analytics

    companion object {
        val TAG = MigrateWalletErrorFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(): MigrateWalletErrorFragment {
            return MigrateWalletErrorFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<MigrateWalletErrorFragmentBinding>(
                inflater, R.layout.migrate_wallet_error_fragment, container, false)
        try {
            binding.model = (activity as MigrateWalletActivity).getModel()
        } catch (e: Exception) {
            Log.e(MigrateWalletFragment.TAG, "Invalid data cant start MigrateWalletErrorFragment")
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