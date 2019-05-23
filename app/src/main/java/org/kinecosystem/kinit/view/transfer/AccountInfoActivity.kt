package org.kinecosystem.kinit.view.transfer


import android.os.Bundle
import org.kinecosystem.kinit.viewmodel.AccountInfoViewModel
import org.kinecosystem.transfer.receiver.view.AccountInfoActivityBase

class AccountInfoActivity : AccountInfoActivityBase() {

    private var viewModel: AccountInfoViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = AccountInfoViewModel()
    }

    override fun getAccountInfo(): String? {
        return viewModel?.getPublicAddress()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel = null
    }

}