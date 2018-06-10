package org.kinecosystem.kinit.viewmodel.spend

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.spend.Offer
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.network.OperationCompletionCallback
import org.kinecosystem.kinit.network.ServicesProvider
import org.kinecosystem.kinit.repository.OffersRepository
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

    fun getOffers(): List<Offer> {
        return offersRepository.offerList
    }

    private fun refresh() {
        if (servicesProvider.isNetworkConnected()) {
            hasNetwork.set(true)
            hasOffers.set(!offersRepository.offerList.isEmpty())
            showNoOffer.set(!hasOffers.get())
            balance.set(servicesProvider.walletService.balance.get().toString())
            if (!hasOffers.get()) {
                servicesProvider.offerService.retrieveOffers(object : OperationCompletionCallback {
                    override fun onError(errorCode: Int) {
                    }

                    override fun onSuccess() {
                        hasOffers.set(!offersRepository.offerList.isEmpty())
                    }
                })
            }
        } else {
            hasNetwork.set(false)
            hasOffers.set(false)
            showNoOffer.set(false)
        }
    }

    override fun onScreenVisibleToUser() {
        refresh()
        val event: Events.Event =
            if (!hasNetwork.get()) {
                Events.Analytics.ViewErrorPage(Analytics.VIEW_ERROR_TYPE_INTERNET_CONNECTION)
            } else if (showNoOffer.get()) {
                Events.Analytics.ViewEmptyStatePage(Analytics.MENU_ITEM_NAME_EARN)
            } else {
                Events.Analytics.ViewSpendPage(offersRepository.numOfOffers())
            }
        analytics.logEvent(event)
    }

    fun onItemClicked(position: Int) {
        navigator.navigateTo(Navigator.Destination.SPEND, position)
        val offer = getOffers()[position]
        analytics.logEvent(Events.Analytics.ClickOfferItemOnSpendPage(offer.provider?.name,
            offer.price,
            offersRepository.numOfOffers(),
            offer.domain,
            offer.id,
            offer.title,
            position,
            offer.type))
    }

}

