package org.kinecosystem.kinit.viewmodel.spend

import android.databinding.ObservableField
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.repository.OffersRepository
import org.kinecosystem.kinit.server.NetworkServices
import org.kinecosystem.kinit.view.TabViewModel
import javax.inject.Inject

class SpendTabsViewModel(val listener: TabStateListener) :
        TabViewModel {

    @Inject
    lateinit var analytics: Analytics
    @Inject
    lateinit var networkServices: NetworkServices
    @Inject
    lateinit var offersRepository: OffersRepository

    var balance: ObservableField<String>

    init {
        KinitApplication.coreComponent.inject(this)
        balance = networkServices.walletService.balance
    }

    override fun onScreenVisibleToUser() {
       val index = listener.getCurrentTabIndex()
        if (index == 1) {
            analytics.logEvent(Events.Analytics.ViewExplorePage())
        } else if (index == 0) {
            val event: Events.Event =
                    if (!networkServices.isNetworkConnected()) {
                        Events.Analytics.ViewErrorPage(Analytics.VIEW_ERROR_TYPE_INTERNET_CONNECTION, "no internet")
                    } else if (offersRepository.offers.get().isEmpty()) {
                        Events.Analytics.ViewEmptyStatePage(Analytics.MENU_ITEM_NAME_USE_KIN, "")
                    } else {
                        Events.Analytics.ViewSpendPage(offersRepository.offersCount())
                    }
            analytics.logEvent(event)
        }
    }


}

interface TabStateListener {
    fun getCurrentTabIndex(): Int
}

