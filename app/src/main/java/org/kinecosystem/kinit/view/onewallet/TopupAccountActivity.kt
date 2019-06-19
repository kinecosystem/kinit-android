package org.kinecosystem.kinit.view.onewallet

import android.annotation.SuppressLint
import android.os.Bundle
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.blockchain.TopupResult
import org.kinecosystem.transfer.amountChooser.TransferAmountActivityBase


class TopupAccountActivity : TransferAmountActivityBase(), TopupViewActions {

    private lateinit var viewModel: TopupAccountViewModel

    companion object {
        val EXTRA_APP_ID = "EXTRA_APP_ID"
        val EXTRA_PUBLIC_ADDRESS = "EXTRA_PUBLIC_ADDRESS"
    }

    // not clear why lint is warning about missing super call - since it's there!
    @SuppressLint("MissingSuperCall")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dstAppId = intent.extras.getString(EXTRA_APP_ID)
        val dstPublicAddress = intent.extras.getString(EXTRA_PUBLIC_ADDRESS)
        viewModel = TopupAccountViewModel(dstAppId, dstPublicAddress, this)
    }

//    override protected fun layoutId(): Int {
//        return R.layout.transfer_amount_layout
//    }

    override fun displayInProgress() {
        updateButtonText(R.string.topup_in_progress)
    }

    override fun displaySuccess(topupResult: TopupResult) {
        updateButtonText(R.string.topup_success)
    }

    override fun displayError(errorCode: Int) {
        updateButtonText(R.string.topup_failed)
    }

    override fun onTransferKinButtonClicked(amount: Int) {
        viewModel.onTransferKinButtonClicked(amount)
    }

    override fun buttonStringRes(): Int {
        return R.string.topup_button
    }

    override fun titleStringRes(): Int {
        return R.string.topup_title_to
    }

}