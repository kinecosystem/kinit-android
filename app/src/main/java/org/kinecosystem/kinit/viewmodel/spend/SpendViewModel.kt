package org.kinecosystem.kinit.viewmodel.spend

import android.databinding.ObservableBoolean
import org.kinecosystem.kinit.CoreComponentsProvider
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.spend.Offer
import org.kinecosystem.kinit.navigation.Navigator

class SpendViewModel(private val coreComponents: CoreComponentsProvider, private val navigator: Navigator) {
    val questionnaireRepo = coreComponents.questionnaireRepo()

    var balance = coreComponents.services().walletService.balance
    var hasOffers = ObservableBoolean(false)
    var showNoOffer = ObservableBoolean(false)
    var hasNetwork = ObservableBoolean(coreComponents.services().isNetworkConnected())
    val analytics = coreComponents.analytics()

    fun getOffers(): List<Offer> {
        return coreComponents.offersRepo().offerList
    }

    fun onResume() {
        if (coreComponents.services().isNetworkConnected()) {
            hasNetwork.set(true)
            hasOffers.set(!coreComponents.offersRepo().offerList.isEmpty())
            showNoOffer.set(!hasOffers.get())
            balance.set(coreComponents.services().walletService.balance.get().toString())
            if (!hasOffers.get()) {
                coreComponents.services().offerService.retrieveOffers()
            }
        } else {
            hasNetwork.set(false)
            hasOffers.set(false)
            showNoOffer.set(false)
        }
    }

    fun onScreenVisibleToUser() {
        val event: Events.Event =
            if (!hasNetwork.get()) {
                Events.Analytics.ViewErrorPage(Analytics.VIEW_ERROR_TYPE_INTERNET_CONNECTION)
            } else if (showNoOffer.get()) {
                Events.Analytics.ViewEmptyStatePage(Analytics.MENU_ITEM_NAME_EARN)
            } else {
                Events.Analytics.ViewSpendPage(coreComponents.offersRepo().numOfOffers())
            }
        analytics.logEvent(event)
    }

    fun onItemClicked(position: Int) {
        navigator.navigateTo(Navigator.Destination.SPEND, position)
        val offer = getOffers()[position]
        analytics.logEvent(Events.Analytics.ClickOfferItemOnSpendPage(offer.provider?.name,
            offer.price,
            coreComponents.offersRepo().numOfOffers(),
            offer.domain,
            offer.id,
            offer.title,
            position,
            offer.type))
    }

}

