package org.kinecosystem.tippic.viewmodel.spend

import android.databinding.ObservableField
import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.server.NetworkServices
import javax.inject.Inject

class SpendTabsViewModel {

    @Inject
    lateinit var networkServices: NetworkServices

    var balance: ObservableField<String>

    init {
        TippicApplication.coreComponent.inject(this)
        balance = networkServices.walletService.balance
    }

}