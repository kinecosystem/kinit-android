package org.kinecosystem.kinit.viewmodel.spend

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.view.View
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.spend.Offer
import org.kinecosystem.kinit.model.spend.isP2p
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.repository.TasksRepository
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.server.ERROR_NO_INTERNET
import org.kinecosystem.kinit.server.ERROR_REDEEM_COUPON_FAILED
import org.kinecosystem.kinit.server.NetworkServices
import org.kinecosystem.kinit.server.OperationResultCallback
import org.kinecosystem.kinit.view.spend.PurchaseOfferActions
import javax.inject.Inject

class PurchaseOfferViewModel(private val navigator: Navigator, val offer: Offer) {

    @Inject
    lateinit var analytics: Analytics
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var networkServices: NetworkServices
    @Inject
    lateinit var tasksRepository: TasksRepository

    var title: String?
    var info: String?
    var price: String?
    val typeImageUrl: String?
    val headerImageUrl: String?
    val providerImageUrl: String?
    val isP2p: Boolean
    val couponCode = ObservableField("")
    val couponPurchaseCompleted = ObservableBoolean(false)
    var purchaseOfferActions: PurchaseOfferActions? = null
    var canBuy: ObservableBoolean = ObservableBoolean(false)

    init {
        KinitApplication.coreComponent.inject(this)
        title = offer.title
        info = offer.description
        price = offer.price.toString()
        typeImageUrl = offer.imageTypeUrl
        headerImageUrl = offer.imageUrl
        providerImageUrl = offer.provider?.imageUrl
        isP2p = offer.isP2p()
    }


    fun onShareButtonClicked(view: View) {
        analytics.logEvent(
            Events.Analytics.ClickShareButtonOnOfferPage(offer.provider?.name, offer.price, offer.domain, offer.id,
                offer.title, offer.type))
        purchaseOfferActions?.shareCode(couponCode.get())
    }

    fun onBuyButtonClicked(view: View) {
        analytics.logEvent(
            Events.Analytics.ClickBuyButtonOnOfferPage(offer.provider?.name, offer.price, offer.domain, offer.id,
                offer.title, offer.type))
        if (!networkServices.isNetworkConnected()) {
            purchaseOfferActions?.showDialog(
                R.string.dialog_no_internet_title, R.string.dialog_no_internet_message,
                R.string.dialog_ok, false, Analytics.VIEW_ERROR_TYPE_INTERNET_CONNECTION)
            return
        }
        if (isP2p) {
            navigator.navigateTo(Navigator.Destination.PEER2PEER)
        } else {
            purchaseOfferActions?.animateBuy()
            buy()
        }
    }

    fun onCloseButtonClicked(view: View?) {
        navigator.navigateTo(Navigator.Destination.MAIN_SCREEN)
        purchaseOfferActions?.closeScreen()
    }

    private fun buy() {
        couponCode.set("")
        couponPurchaseCompleted.set(false)
        networkServices.offerService.buyOffer(offer, object : OperationResultCallback<String> {
            override fun onResult(result: String) {
                couponCode.set(result)
                couponPurchaseCompleted.set(true)
                networkServices.walletService.retrieveTransactions()
                networkServices.walletService.retrieveCoupons()
                analytics.logEvent(
                    Events.Analytics.ViewCodeTextOnOfferPage(offer.provider?.name, offer.price, offer.domain, offer.id,
                        offer.title, offer.type))
            }

            override fun onError(errorCode: Int) {
                val logErrorType: String
                when (errorCode) {
                    ERROR_NO_INTERNET -> {
                        logErrorType = Analytics.VIEW_ERROR_TYPE_INTERNET_CONNECTION
                        purchaseOfferActions?.showDialog(R.string.dialog_no_internet_title,
                            R.string.dialog_no_internet_message, R.string.dialog_ok, false, logErrorType)
                    }
                    ERROR_REDEEM_COUPON_FAILED -> {
                        logErrorType = Analytics.VIEW_ERROR_TYPE_CODE_NOT_PROVIDED
                        purchaseOfferActions?.showDialog(
                            R.string.dialog_coupon_redeem_failed_title, R.string.dialog_coupon_redeem_failed_message,
                            R.string.dialog_back_to_list, true, logErrorType)
                    }
                    else -> { // ERROR_NO_GOOD_LEFT, ERROR_TRANSACTION_FAILED
                        logErrorType = Analytics.VIEW_ERROR_TYPE_OFFER_NOT_AVAILABLE
                        purchaseOfferActions?.showDialog(R.string.dialog_no_good_left_title,
                            R.string.dialog_no_good_left_message, R.string.dialog_back_to_list, true, logErrorType)
                    }
                }
            }
        })
    }

    fun onResume() {
        val offerPrice = offer.price ?: Int.MAX_VALUE
        val numOfTasks = tasksRepository.task?.id?.toInt() ?: Integer.MAX_VALUE
        var offerEnabled = true
        if (isP2p) {
            offerEnabled = numOfTasks >= userRepository.p2pMinTasks
        }

        canBuy.set(networkServices.walletService.balanceInt >= offerPrice && offerEnabled)

        analytics.logEvent(Events.Analytics.ViewOfferPage(offer.provider?.name,
            offer.price,
            offer.domain,
            offer.id,
            offer.title, offer.type))
        if (isP2p) {
            purchaseOfferActions?.updateBuyButtonWidth()
        }
    }

    fun logViewErrorPopup(errorType: String) {
        analytics.logEvent(Events.Analytics.ViewErrorPopupOnOfferPage(errorType))
    }

    fun logCloseErrorPopupClicked(errorType: String) {
        analytics.logEvent(Events.Analytics.ClickOkButtonOnErrorPopup(errorType))
    }

}