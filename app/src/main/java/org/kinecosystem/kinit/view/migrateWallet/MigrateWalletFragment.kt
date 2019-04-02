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
import org.kinecosystem.kinit.view.createWallet.CreateWalletActivity
import org.kinecosystem.kinit.view.createWallet.CreateWalletFragment
import org.kinecosystem.kinit.viewmodel.CreateWalletViewModel
import javax.inject.Inject

class MigrateWalletFragment : BaseFragment() {
    @Inject
    lateinit var analytics: Analytics
    private lateinit var model: CreateWalletViewModel

    companion object {
        val TAG = CreateWalletFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(): CreateWalletFragment {
            return CreateWalletFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        try {
            model = (activity as CreateWalletActivity).getModel()
        } catch (e: Exception) {
            Log.e(CreateWalletFragment.TAG, "Invalid data cant start CreateWalletFragment")
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
        model.initWallet()
    }

    override fun onResume() {
        super.onResume()
        analytics.logEvent(Events.Analytics.ViewCreatingWalletPage())
    }
}