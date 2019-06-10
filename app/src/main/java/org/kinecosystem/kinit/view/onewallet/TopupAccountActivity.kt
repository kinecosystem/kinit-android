package org.kinecosystem.kinit.view.onewallet

import android.os.Bundle
import android.widget.TextView
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.blockchain.TopupManager
import org.kinecosystem.kinit.blockchain.Wallet
import org.kinecosystem.transfer.receiver.view.AccountInfoActivityBase
import javax.inject.Inject

// TODO change the activity ui completely. It should not extend AccountInfoActivityBase...
// This is not the real TopupAccountActivity yet
// TEMPORARILY using the 'AccountInfoActivityBase' to provide temp mechanism to test the topup logic
//
// How this works:
// When getData() is called a topup of 100Kin (hardcoded) is moved from the linked accounts to the calling app account.
class TopupAccountActivity : AccountInfoActivityBase() {
    companion object {
        val EXTRA_APP_ID = "EXTRA_APP_ID"
        val EXTRA_PUBLIC_ADDRESS = "EXTRA_PUBLIC_ADDRESS"
        val EXTRA_AMOUNT = "EXTRA_AMOUNT"
    }

    @Inject
    lateinit var wallet: Wallet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KinitApplication.coreComponent.inject(this)
    }

    override fun getData(): String {
        val dstAppId = intent.extras.getString(EXTRA_APP_ID)
        val dstPublicAddress = intent.extras.getString(EXTRA_PUBLIC_ADDRESS)
        val amount = Integer.parseInt(intent.extras.getString(EXTRA_AMOUNT, "5"))
        return wallet.topupManager.topup(amount, dstAppId, dstPublicAddress).toString()
    }

    override fun updateSourceApp(sourceApp: String) {
        val title = findViewById<TextView>(R.id.transfer_title)
        title.text =  "When you click on confirm a topup of 100 Kin will start. Kinit will use linked accounts from other apps " +
            "to transfer the 100 Kin to $sourceApp"
    }

}