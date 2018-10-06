package org.kinecosystem.kinit.viewmodel.spend

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.spend.Offer
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.repository.OffersRepository
import org.kinecosystem.kinit.server.OperationCompletionCallback
import org.kinecosystem.kinit.server.ServicesProvider
import org.kinecosystem.kinit.view.TabViewModel
import javax.inject.Inject

class SpendViewModel(private val navigator: Navigator) :
    TabViewModel {

    @Inject
    lateinit var offersRepository: OffersRepository
    @Inject
    lateinit var analytics: Analytics
    @Inject
    lateinit var servicesProvider: ServicesProvider

    var balance: ObservableField<String>
    var hasOffers = ObservableBoolean(false)
    var showNoOffer = ObservableBoolean(false)
    var hasNetwork = ObservableBoolean(false)


    init {
        KinitApplication.coreComponent.inject(this)
        balance = servicesProvider.walletService.balance
        hasNetwork = ObservableBoolean(servicesProvider.isNetworkConnected())
        refresh()
    }

    fun offers(): List<Offer> {
        return offersRepository.offers.get()
    }

    private fun refresh() {
        if (servicesProvider.isNetworkConnected()) {
            hasNetwork.set(true)
            hasOffers.set(!offersRepository.isEmpty())
            showNoOffer.set(!hasOffers.get())
            balance.set(servicesProvider.walletService.balance.get().toString())
        } else {
            hasNetwork.set(false)
            hasOffers.set(false)
            showNoOffer.set(false)
        }
    }

    private fun checkForUpdates() {
        if (hasNetwork.get()) {
            servicesProvider.offerService.retrieveOffers(object : OperationCompletionCallback {
                override fun onError(errorCode: Int) {
                }

                override fun onSuccess() {
                    refresh()
                }
            })
        }
    }

    override fun onScreenVisibleToUser() {
        refresh()
        checkForUpdates()
        val event: Events.Event =
            if (!hasNetwork.get()) {
                Events.Analytics.ViewErrorPage(Analytics.VIEW_ERROR_TYPE_INTERNET_CONNECTION)
            } else if (showNoOffer.get()) {
                Events.Analytics.ViewEmptyStatePage(Analytics.MENU_ITEM_NAME_EARN)
            } else {
                Events.Analytics.ViewSpendPage(offersRepository.offersCount())
            }
        analytics.logEvent(event)
    }

    fun onItemClicked(offer: Offer, position: Int) {
        navigator.navigateTo(offer)
        analytics.logEvent(Events.Analytics.ClickOfferItemOnSpendPage(offer.provider?.name,
            offer.price,
            offersRepository.offersCount(),
            offer.domain,
            offer.id,
            offer.title,
            position,
            offer.type))
    }

}

