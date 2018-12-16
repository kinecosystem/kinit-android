package org.kinecosystem.kinit.viewmodel.spend

import android.databinding.ObservableField
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.server.NetworkServices
import javax.inject.Inject

class SpendTabsViewModel {

    @Inject
    lateinit var networkServices: NetworkServices

    var balance: ObservableField<String>

    init {
        KinitApplication.coreComponent.inject(this)
        balance = networkServices.walletService.balance
    }

}