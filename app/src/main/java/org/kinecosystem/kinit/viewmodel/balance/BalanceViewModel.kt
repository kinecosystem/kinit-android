package org.kinecosystem.kinit.viewmodel.balance

import android.databinding.ObservableBoolean
import android.support.design.widget.TabLayout
import org.kinecosystem.kinit.CoreComponentsProvider
import org.kinecosystem.kinit.analytics.Events

class BalanceViewModel(private val coreComponents: CoreComponentsProvider) {
    val balance = coreComponents.services().walletService.balance
    val hasTransactions = ObservableBoolean(false)
    val hasCoupons = ObservableBoolean(false)
    val showNoTransaction = ObservableBoolean(false)
    val hasNetwork = ObservableBoolean(coreComponents.services().isNetworkConnected())
    val analytics = coreComponents.analytics()

    fun onResume() {
        if (coreComponents.services().isNetworkConnected()) {
            hasNetwork.set(true)
            hasTransactions.set(!coreComponents.services().walletService.transactions.get().isEmpty())
            hasCoupons.set(!coreComponents.services().walletService.coupons.get().isEmpty())
            showNoTransaction.set(!hasTransactions.get())
            balance.set(coreComponents.services().walletService.balance.get().toString())
        } else {
            hasNetwork.set(false)
            hasTransactions.set(false)
            showNoTransaction.set(false)
        }
    }

    fun onScreenVisibleToUser() {
        coreComponents.analytics().logEvent(Events.Analytics.ViewBalancePage())
    }
}
