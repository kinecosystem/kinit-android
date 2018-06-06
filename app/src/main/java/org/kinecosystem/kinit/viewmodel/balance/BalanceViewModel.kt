package org.kinecosystem.kinit.viewmodel.balance

import android.databinding.ObservableBoolean
import org.kinecosystem.kinit.CoreComponentsProvider
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.view.TabViewModel

class BalanceViewModel(private val coreComponents: CoreComponentsProvider) : TabViewModel {
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

    override fun onScreenVisibleToUser() {
        hasTransactions.set(!coreComponents.services().walletService.transactions.get().isEmpty())
        hasCoupons.set(!coreComponents.services().walletService.coupons.get().isEmpty())
        coreComponents.analytics().logEvent(Events.Analytics.ViewBalancePage())
    }
}
