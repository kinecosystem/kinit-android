package org.kinecosystem.tippic.view.restore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.welcomeback_restore_wallet_fragment.*
import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.R
import org.kinecosystem.tippic.analytics.Analytics
import org.kinecosystem.tippic.analytics.Events
import org.kinecosystem.tippic.view.BaseFragment
import org.kinecosystem.tippic.viewmodel.restore.RestoreWalletActivityMessages
import javax.inject.Inject

class RestoreWalletWelcomebackFragment : BaseFragment() {
    private lateinit var actions: RestoreWalletActions

    @Inject
    lateinit var analytics: Analytics

    companion object {
        val TAG = RestoreWalletWelcomebackFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(): RestoreWalletWelcomebackFragment {
            return RestoreWalletWelcomebackFragment()
        }
    }

    init {
        TippicApplication.coreComponent.inject(this)
    }

    override fun onResume() {
        super.onResume()
        actions.getModel().onResume()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        try {
            actions = activity as RestoreWalletActions
        } catch (e: Exception) {
            Log.e(TAG, "activity must and implement RestoreWalletActions")
            activity?.finish()
        }
        return inflater.inflate(R.layout.welcomeback_restore_wallet_fragment, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restore?.setOnClickListener {
            analytics.logEvent(Events.Analytics.ClickRestoreWalletButtonOnWelcomeBackPage())
            actions.moveToNextScreen()
        }
        create_new_wallet?.setOnClickListener {
            analytics.logEvent(Events.Analytics.ClickCreateNewWalletButtonOnWelcomeBackPage())
            actions.showDialog(RestoreWalletActivityMessages.CREATE_WALLET_WARNING)
        }
    }
}