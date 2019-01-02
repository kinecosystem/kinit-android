package org.kinecosystem.tippic.viewmodel.balance

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.analytics.Analytics
import org.kinecosystem.tippic.analytics.Events
import org.kinecosystem.tippic.server.NetworkServices
import org.kinecosystem.tippic.view.TabViewModel
import javax.inject.Inject

class BalanceViewModel : TabViewModel {

    var balance: ObservableField<String>
    val hasTransactions = ObservableBoolean(false)
    val hasCoupons = ObservableBoolean(false)
    private val showNoTransaction = ObservableBoolean(false)
    var hasNetwork: ObservableBoolean
    @Inject
    lateinit var networkServices: NetworkServices
    @Inject
    lateinit var analytics: Analytics

    init {
        TippicApplication.coreComponent.inject(this)
        balance = networkServices.offerService.wallet.balance
        hasNetwork = ObservableBoolean(networkServices.isNetworkConnected())
    }

    fun onResume() {
        if (networkServices.isNetworkConnected()) {
            hasNetwork.set(true)
            hasTransactions.set(!networkServices.walletService.transactions.get().isEmpty())
            hasCoupons.set(!networkServices.walletService.coupons.get().isEmpty())
            showNoTransaction.set(!hasTransactions.get())
            balance.set(networkServices.walletService.balance.get().toString())
        } else {
            hasNetwork.set(false)
            hasTransactions.set(false)
            showNoTransaction.set(false)
        }
    }

    override fun onScreenVisibleToUser() {
        hasTransactions.set(!networkServices.walletService.transactions.get().isEmpty())
        hasCoupons.set(!networkServices.walletService.coupons.get().isEmpty())
        analytics.logEvent(Events.Analytics.ViewBalancePage())
    }
}
