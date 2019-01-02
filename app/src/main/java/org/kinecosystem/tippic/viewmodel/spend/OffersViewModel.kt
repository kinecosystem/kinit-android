package org.kinecosystem.tippic.viewmodel.spend

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.analytics.Analytics
import org.kinecosystem.tippic.analytics.Events
import org.kinecosystem.tippic.model.spend.Offer
import org.kinecosystem.tippic.navigation.Navigator
import org.kinecosystem.tippic.repository.OffersRepository
import org.kinecosystem.tippic.repository.UserRepository
import org.kinecosystem.tippic.server.ERROR_APP_SERVER_FAILED_RESPONSE
import org.kinecosystem.tippic.server.ERROR_EMPTY_RESPONSE
import org.kinecosystem.tippic.server.NetworkServices
import org.kinecosystem.tippic.server.OperationCompletionCallback
import org.kinecosystem.tippic.view.TabViewModel
import javax.inject.Inject

class OffersViewModel(private val navigator: Navigator) :
        TabViewModel {

    @Inject
    lateinit var offersRepository: OffersRepository
    @Inject
    lateinit var analytics: Analytics
    @Inject
    lateinit var networkServices: NetworkServices
    @Inject
    lateinit var userRepository: UserRepository

    var balance: ObservableField<String>

    val hasErrors = ObservableBoolean(false)
    val hasNetwork = ObservableBoolean(true)
    val showData = ObservableBoolean(true)
    val isLoading = ObservableBoolean(false)
    var showNewOfferPolicyAlert = ObservableBoolean(false)

    init {
        TippicApplication.coreComponent.inject(this)
        balance = networkServices.walletService.balance
        hasNetwork.set(networkServices.isNetworkConnected())
        bindData()
    }

    fun offers(): List<Offer> {
        return offersRepository.offers.get()
    }

    fun onDataLoaded() {
        isLoading.set(false)
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
                    isLoading.set(false)
                    showData.set(false)
                    hasErrors.set(true)
                    val reason = when (errorCode) {
                        ERROR_APP_SERVER_FAILED_RESPONSE -> Analytics.SERVER_ERROR_RESPONSE
                        ERROR_EMPTY_RESPONSE -> Analytics.SERVER_EMPTY_RESPONSE
                        else -> Analytics.SERVER_ERROR_RESPONSE
                    }
                    Events.Analytics.ViewErrorPage(Analytics.VIEW_ERROR_TYPE_GENERIC, reason)
                }

                override fun onSuccess() {
                    isLoading.set(false)
                    hasErrors.set(false)
                    showData.set(true)
                    bindData()
                }
            })
        }
    }

    override fun onScreenVisibleToUser() {
        if (offersRepository.isEmpty()) {
            isLoading.set(true)
        }
        bindData()
        checkForUpdates()
        if (!userRepository.seenNewSPendPolicy) {
            showNewOfferPolicyAlert.set(true)
            userRepository.seenNewSPendPolicy = true
        }
        val event: Events.Event =
                if (!hasNetwork.get()) {
                    Events.Analytics.ViewErrorPage(Analytics.VIEW_ERROR_TYPE_INTERNET_CONNECTION, "no internet")
                } else if (offersRepository.offers.get().isEmpty()) {
                    Events.Analytics.ViewEmptyStatePage(Analytics.MENU_ITEM_NAME_USE_KIN, "")
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

