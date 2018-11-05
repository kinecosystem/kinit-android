package org.kinecosystem.kinit.viewmodel.spend

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.spend.Offer
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.repository.OffersRepository
import org.kinecosystem.kinit.server.NetworkServices
import org.kinecosystem.kinit.server.OperationCompletionCallback
import org.kinecosystem.kinit.view.TabViewModel
import javax.inject.Inject

class SpendViewModel(private val navigator: Navigator) :
        TabViewModel {

    @Inject
    lateinit var offersRepository: OffersRepository
    @Inject
    lateinit var analytics: Analytics
    @Inject
    lateinit var networkServices: NetworkServices

    var balance: ObservableField<String>

    var hasErrors = ObservableBoolean(false)
    var hasNetwork = ObservableBoolean(true)
    val showData = ObservableBoolean(true)


    init {
        KinitApplication.coreComponent.inject(this)
        balance = networkServices.walletService.balance
        hasNetwork.set(networkServices.isNetworkConnected())
        bindData()
    }

    fun offers(): List<Offer> {
        return offersRepository.offers.get()
    }

    private fun bindData() {
        if (networkServices.isNetworkConnected()) {
            hasNetwork.set(true)
            showData.set(true)
            balance.set(networkServices.walletService.balance.get().toString())
        } else {
            hasNetwork.set(false)
            showData.set(false)
        }
    }

    private fun checkForUpdates() {
        if (hasNetwork.get()) {
            networkServices.offerService.retrieveOffers(object : OperationCompletionCallback {
                override fun onError(errorCode: Int) {
                    showData.set(false)
                    hasErrors.set(true)
                }

                override fun onSuccess() {
                    hasErrors.set(false)
                    showData.set(true)
                    bindData()
                }
            })
        }
    }

    override fun onScreenVisibleToUser() {
        bindData()
        checkForUpdates()
        val event: Events.Event =
                if (!hasNetwork.get()) {
                    Events.Analytics.ViewErrorPage(Analytics.VIEW_ERROR_TYPE_INTERNET_CONNECTION, "no internet")
                } else if (offersRepository.offers.get().isEmpty()) {
                    Events.Analytics.ViewEmptyStatePage(Analytics.MENU_ITEM_NAME_EARN, "")
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

