package org.kinecosystem.kinit.viewmodel.balance

import android.databinding.ObservableField
import org.kinecosystem.kinit.CoreComponentsProvider
import org.kinecosystem.kinit.analytics.Events

class BalanceViewModel(private val coreComponents: CoreComponentsProvider) {

    var balance: ObservableField<String> = coreComponents.services().walletService.balance

    fun onScreenVisibleToUser() {
        coreComponents.analytics().logEvent(Events.Analytics.ViewBalancePage())
    }
}
