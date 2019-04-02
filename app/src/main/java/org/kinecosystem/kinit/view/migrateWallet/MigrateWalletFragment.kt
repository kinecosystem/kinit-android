package org.kinecosystem.kinit.view.migrateWallet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.view.BaseFragment
import org.kinecosystem.kinit.viewmodel.MigrateWalletViewModel
import javax.inject.Inject

class MigrateWalletFragment : BaseFragment() {
    @Inject
    lateinit var analytics: Analytics
    private lateinit var model: MigrateWalletViewModel

    companion object {
        val TAG = MigrateWalletFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(): MigrateWalletFragment {
            return MigrateWalletFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        try {
            model = (activity as MigrateWalletActivity).getModel()
        } catch (e: Exception) {
            Log.e(MigrateWalletFragment.TAG, "Invalid data cant start MigrateWalletFragment")
            activity?.finish()
        }
        return inflater.inflate(R.layout.create_wallet_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        KinitApplication.coreComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.migrateWallet()
    }

    override fun onResume() {
        super.onResume()
        analytics.logEvent(Events.Analytics.ViewCreatingWalletPage())
    }
}