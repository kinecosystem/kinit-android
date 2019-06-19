package org.kinecosystem.kinit.view.onewallet

import android.os.Bundle
import android.widget.TextView
import com.google.gson.Gson
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.blockchain.Wallet
import org.kinecosystem.transfer.receiver.view.AccountInfoActivityBase
import javax.inject.Inject

class LinkAccountActivity : AccountInfoActivityBase() {
    companion object {
        val EXTRA_APP_ID = "EXTRA_APP_ID"
        val EXTRA_PUBLIC_ADDRESS = "EXTRA_PUBLIC_ADDRESS"
    }

    @Inject
    lateinit var wallet: Wallet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KinitApplication.coreComponent.inject(this)
    }

    override fun getData(): String? {
        val appId = intent.extras.getString(EXTRA_APP_ID)
        val publicAddress = intent.extras.getString(EXTRA_PUBLIC_ADDRESS)
        return wallet.oneWallet.getLinkingTransactionEnvelopeFor(appId, publicAddress)?.let { linkingResult ->
            return Gson().toJson(linkingResult)
        }
    }

    override fun updateSourceApp(sourceApp: String) {
        val title = findViewById<TextView>(R.id.transfer_title)
        title.text = getString(R.string.linking_activity_confirmation_message, sourceApp)
    }

}