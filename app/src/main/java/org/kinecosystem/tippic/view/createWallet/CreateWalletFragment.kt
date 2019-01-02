package org.kinecosystem.tippic.view.createWallet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.R
import org.kinecosystem.tippic.analytics.Analytics
import org.kinecosystem.tippic.analytics.Events
import org.kinecosystem.tippic.view.BaseFragment
import org.kinecosystem.tippic.viewmodel.CreateWalletViewModel
import javax.inject.Inject

class CreateWalletFragment : BaseFragment() {
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
        TippicApplication.coreComponent.inject(this)
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