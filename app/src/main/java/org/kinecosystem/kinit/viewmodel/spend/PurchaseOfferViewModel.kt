package org.kinecosystem.kinit.viewmodel.spend

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.view.View
import org.kinecosystem.kinit.CoreComponentsProvider
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.spend.isP2p
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.network.ERROR_NO_INTERNET
import org.kinecosystem.kinit.network.ERROR_REDEEM_COUPON_FAILED
import org.kinecosystem.kinit.network.OperationResultCallback
import org.kinecosystem.kinit.view.spend.PurchaseOfferActions

class PurchaseOfferViewModel(private val coreComponents: CoreComponentsProvider, private val navigator: Navigator,
    offerIndex: Int) {

    private val offer = coreComponents.offersRepo().offer(offerIndex)
    private val analytics = coreComponents.analytics()
    val title = offer.title
    val info = offer.description
    val price = offer.price.toString()
    val typeImageUrl = offer.imageTypeUrl
    val headerImageUrl = offer.imageUrl
    val providerImageUrl = offer.provider?.imageUrl
    val isP2p = offer.isP2p()
    val couponCode = ObservableField("")
    val couponPurchaseCompleted = ObservableBoolean(false)
    private var canCloseScreen = true
    var purchaseOfferActions: PurchaseOfferActions? = null
    var canBuy: ObservableBoolean = ObservableBoolean(false)


    fun onShareButtonClicked(view: View) {
        analytics.logEvent(
            Events.Analytics.ClickShareButtonOnOfferPage(offer.provider?.name, offer.price, offer.domain, offer.id,
                offer.title, offer.type))
        purchaseOfferActions?.shareCode(couponCode.get())
        canCloseScreen = true
    }

    fun onBuyButtonClicked(view: View) {
        analytics.logEvent(
            Events.Analytics.ClickBuyButtonOnOfferPage(offer.provider?.name, offer.price, offer.domain, offer.id,
                offer.title, offer.type))
        if (!coreComponents.services().isNetworkConnected()) {
            purchaseOfferActions?.showDialog(
                R.string.dialog_no_internet_title, R.string.dialog_no_internet_message,
                R.string.dialog_ok, false, Analytics.VIEW_ERROR_TYPE_INTERNET_CONNECTION)
            return
        }
        if (isP2p) {
            navigator.navigateTo(Navigator.Destination.PEER2PEER)
        } else {
            canCloseScreen = false
            purchaseOfferActions?.animateBuy()
            buy()
        }
    }

    fun onCloseButtonClicked(view: View?) {
        if (canCloseScreen) {
            navigator.navigateTo(Navigator.Destination.MAIN_SCREEN)
            purchaseOfferActions?.closeScreen()
        } else {
            purchaseOfferActions?.showDialog(R.string.dialog_exit_before_share_title,
                R.string.dialog_exit_before_share_message, R.string.dialog_got_it, false)
            canCloseScreen = true
        }
    }

    private fun buy() {
        couponCode.set("")
        couponPurchaseCompleted.set(false)
        coreComponents.services().offerService.buyOffer(offer, object : OperationResultCallback<String> {
            override fun onResult(result: String) {
                couponCode.set(result)
                couponPurchaseCompleted.set(true)
                coreComponents.services().walletService.retrieveTransactions()
                coreComponents.services().walletService.retrieveCoupons()
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
        val taskId = coreComponents.questionnaireRepo().task?.id!!.toInt()
        var p2pValid = true
        if (isP2p && coreComponents.userRepo().isP2pEnabled) {
            p2pValid = coreComponents.userRepo().p2pMinTasks <= taskId
        }
        canBuy.set(coreComponents.services().walletService.balanceInt >= offerPrice && p2pValid)

        coreComponents.analytics().logEvent(Events.Analytics.ViewOfferPage(offer.provider?.name,
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