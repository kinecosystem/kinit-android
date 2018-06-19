package org.kinecosystem.kinit.viewmodel.balance

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import dagger.CoreComponent
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.network.ServicesProvider
import org.kinecosystem.kinit.view.TabViewModel
import javax.inject.Inject

class BalanceViewModel: TabViewModel {

    var balance: ObservableField<String>
    val hasTransactions = ObservableBoolean(false)
    val hasCoupons = ObservableBoolean(false)
    private val showNoTransaction = ObservableBoolean(false)
    var hasNetwork: ObservableBoolean
    @Inject
    lateinit var servicesProvider: ServicesProvider
    @Inject
    lateinit var analytics: Analytics

    init {
        KinitApplication.coreComponent.inject(this)
        balance = servicesProvider.offerService.wallet.balance
        hasNetwork = ObservableBoolean(servicesProvider.isNetworkConnected())
    }

    fun onResume() {
        if (servicesProvider.isNetworkConnected()) {
            hasNetwork.set(true)
            hasTransactions.set(!servicesProvider.walletService.transactions.get().isEmpty())
            hasCoupons.set(!servicesProvider.walletService.coupons.get().isEmpty())
            showNoTransaction.set(!hasTransactions.get())
            balance.set(servicesProvider.walletService.balance.get().toString())
        } else {
            hasNetwork.set(false)
            hasTransactions.set(false)
            showNoTransaction.set(false)
        }
    }

    override fun onScreenVisibleToUser() {
        hasTransactions.set(!servicesProvider.walletService.transactions.get().isEmpty())
        hasCoupons.set(!servicesProvider.walletService.coupons.get().isEmpty())
        analytics.logEvent(Events.Analytics.ViewBalancePage())
    }
}
