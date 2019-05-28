package org.kinecosystem.kinit.view.onewallet

import android.os.Bundle
import android.widget.TextView
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.blockchain.Wallet
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.transfer.receiver.view.AccountInfoActivityBase
import javax.inject.Inject

class LinkAccountActivity : AccountInfoActivityBase() {
    companion object {
        private val EXTRA_APP_PACKAGE_ID = "EXTRA_APP_PACKAGE_ID"
        private val EXTRA_APP_ACCOUNT_PUBLIC_ADDRESS = "EXTRA_APP_ACCOUNT_PUBLIC_ADDRESS"
    }

    @Inject
    lateinit var wallet: Wallet

    @Inject
    lateinit var userRepo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KinitApplication.coreComponent.inject(this)
    }

    override fun getData(): String? {
        val appPackageId = intent.extras.getString(EXTRA_APP_PACKAGE_ID)
        val appAccountPublicAddress = intent.extras.getString(EXTRA_APP_ACCOUNT_PUBLIC_ADDRESS)
        return wallet.getLinkingTransactionEnvelope(appPackageId, appAccountPublicAddress)
    }

    override fun updateSourceApp(sourceApp: String) {
        val title = findViewById<TextView>(R.id.transfer_title)
        title.text = getString(R.string.linking_activity_confirmation_message, sourceApp)
    }

}