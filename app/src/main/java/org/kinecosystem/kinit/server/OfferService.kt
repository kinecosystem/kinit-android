package org.kinecosystem.kinit.server

import android.content.Context
import android.text.TextUtils
import android.util.Log
import kin.sdk.exception.OperationFailedException
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.blockchain.Wallet
import org.kinecosystem.kinit.model.spend.Offer
import org.kinecosystem.kinit.model.spend.TYPE_P2P
import org.kinecosystem.kinit.model.spend.isValid
import org.kinecosystem.kinit.repository.OffersRepository
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.server.api.OffersApi
import org.kinecosystem.kinit.util.GeneralUtils
import org.kinecosystem.kinit.util.Scheduler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.util.*

const val ERROR_TRANSACTION_FAILED = 100
const val ERROR_REDEEM_COUPON_FAILED = 101
const val ERROR_NO_GOOD_LEFT = 200
private const val ERROR_UPDATE_TRANSACTION_TO_SERVER = 300
private const val P2P_ORDER_ID_PREFIX = "p2p"
private const val P2P_TO_APP_ORDER_ID_PREFIX = "to-app"
private const val P2P_ORDER_ID_LENGTH = 10

class OfferService(context: Context, private val offersApi: OffersApi, val userRepo: UserRepository,
                   val repository: OffersRepository, val analytics: Analytics, val wallet: Wallet, val scheduler: Scheduler) {

    val applicationContext: Context = context.applicationContext

    fun retrieveOffers(callback: OperationCompletionCallback? = null) {

        if (!GeneralUtils.isConnected(applicationContext)) {
            return
        }

        offersApi.offers(userRepo.userId()).enqueue(object : Callback<OffersApi.OffersResponse> {
            override fun onResponse(call: Call<OffersApi.OffersResponse>?,
                                    response: Response<OffersApi.OffersResponse>?) {
                if (response != null && response.isSuccessful) {
                    Log.d("OffersService", "onResponse: ${response.body()}")
                    repository.updateOffers(response.body()?.offerList)
                    callback?.onSuccess()
                } else {
                    Log.d("OffersService", "response null or isSuccessful=false: $response")
                    callback?.onError(ERROR_EMPTY_RESPONSE)
                }
            }

            override fun onFailure(call: Call<OffersApi.OffersResponse>?, t: Throwable?) {
                Log.d("TaskService", "onFailure called with throwable $t")
                callback?.onError(ERROR_APP_SERVER_FAILED_RESPONSE)
            }
        })
    }

    fun sendContact(phones: String, callback: OperationResultCallback<String>) {
        if (!GeneralUtils.isConnected(applicationContext)) {
            callback.onError(ERROR_NO_INTERNET)
        }
        scheduler.executeOnBackground({
            val response = offersApi.sendContact(userRepo.userId(), OffersApi.ContactInfo(phones)).execute()
            scheduler.post {
                val address = response.body()?.address.orEmpty()
                if (response.isSuccessful && !TextUtils.isEmpty(address)) {
                    callback.onResult(address)
                } else {
                    callback.onError(ERROR_EMPTY_RESPONSE)
                }
            }
        })
    }

    fun buyOffer(clientValidationJws: String?, offer: Offer, callback: OperationResultCallback<String>) {

        if (!GeneralUtils.isConnected(applicationContext)) {
            callback.onError(ERROR_NO_INTERNET)
        }

        scheduler.executeOnBackground(object : Runnable {

            override fun run() {

                analytics.logEvent(
                        Events.Business.SpendingOfferRequested(offer.provider?.name,
                                offer.price, offer.domain, offer.id,
                                offer.title, offer.type))

                val response: Response<OffersApi.BookOfferResponse>
                try {
                    response = offersApi.bookOffer(userRepo.userId(),
                            OffersApi.OfferInfo(offer.id!!)).execute()
                } catch (e: SocketTimeoutException) {
                    callbackWithError(ERROR_NO_INTERNET)
                    return
                }

                if (!response.isSuccessful || response.body() == null) {
                    callbackWithError(ERROR_NO_GOOD_LEFT)
                    return
                }

                val bookOfferResponse = response.body()
                if (bookOfferResponse?.orderId.orEmpty().isBlank() || bookOfferResponse?.status != "ok" || !offer.isValid()) {
                    when {
                        bookOfferResponse?.reason.equals("no-goods") -> {
                            callbackWithError(ERROR_NO_GOOD_LEFT)
                        }
                        else -> {
                            callbackWithError(ERROR_TRANSACTION_FAILED)
                        }
                    }
                }

                try {
                    val transactionId = wallet.payForOrderSync(clientValidationJws, offer.address!!, offer.price!!,
                            bookOfferResponse?.orderId!!)

                    wallet.updateBalanceSync()
                    wallet.logSpendTransactionCompleted(offer.price, transactionId!!.id())

                    val response2 = offersApi.redeemOffer(userRepo.userId(),
                            OffersApi.PaymentReceipt(transactionId.id())).execute()

                    if (!response2.isSuccessful || response2.body() == null) {
                        callbackWithError(ERROR_REDEEM_COUPON_FAILED)
                    }

                    val redeemResponse: OffersApi.RedeemResponse? = response2.body()
                    if (redeemResponse?.goods.orEmpty().isEmpty() || redeemResponse?.status != "ok") {
                        callbackWithError(ERROR_REDEEM_COUPON_FAILED)
                    }

                    val couponCode: String = redeemResponse?.goods?.get(0)?.value ?: ""
                    if (couponCode.isBlank()) {
                        callbackWithError(ERROR_REDEEM_COUPON_FAILED)
                    }

                    scheduler.post {
                        callback.onResult(couponCode)
                        analytics.logEvent(
                                Events.Business.SpendingOfferProvided(offer.provider?.name,
                                        offer.price, offer.domain,
                                        offer.id, offer.title, offer.type))
                    }
                    Log.d("OfferService", "The coupon is: $couponCode")

                } catch (e: OperationFailedException) {
                    callbackWithError(ERROR_TRANSACTION_FAILED)
                    analytics.logEvent(
                            Events.Business.KINTransactionFailed(
                                    "Spend failed. Exception $e with message ${e.message}",
                                    offer.price, Analytics.TRANSACTION_TYPE_SPEND))
                } catch (e: Exception) {
                    callbackWithError(ERROR_TRANSACTION_FAILED)
                }
            }

            private fun callbackWithError(e: Int) {
                scheduler.post {
                    callback.onError(e)
                }
            }
        })

    }

    fun p2pTransfer(validationToken: String?, toAddress: String, amount: Int, callback: OperationResultCallback<String>) {
        transfer(validationToken, toAddress, amount, callback, false)
    }

    fun app2appTransfer(validationToken: String?, toAddress: String, appSid: String, amount: Int, callback: OperationResultCallback<String>) {
        transfer(validationToken, toAddress, amount, callback, true, appSid)
    }

    private fun transfer(validationToken: String?, toAddress: String, amount: Int, callback: OperationResultCallback<String>, isApp2app: Boolean = false, appSid: String = "") {
        if (!GeneralUtils.isConnected(applicationContext)) {
            callback.onError(ERROR_NO_INTERNET)
        }
        scheduler.executeOnBackground(object : Runnable {
            override fun run() {
                try {
                    val prefix = if (isApp2app) P2P_TO_APP_ORDER_ID_PREFIX else P2P_ORDER_ID_PREFIX
                    val orderId = prefix + UUID.randomUUID().toString().subSequence(0, P2P_ORDER_ID_LENGTH)
                    val transactionId = wallet.payForOrderSync(validationToken, toAddress, amount, orderId)
                    wallet.updateBalanceSync()

                    val txId = transactionId!!.id()
                    scheduler.post {
                        callback.onResult(txId)
                    }
                    wallet.logP2pTransactionCompleted(amount, txId, isApp2app)
                    //update to the server the transactionId
                    if (isApp2app) {
                        offersApi.sendToAppTransactionInfo(userRepo.userId(),
                                OffersApi.AppsTransactionInfo(txId, toAddress, amount, appSid)).execute()
                    } else {
                        offersApi.sendTransactionInfo(userRepo.userId(),
                                OffersApi.PeersTransactionInfo(txId, toAddress, amount)).execute()
                    }
                    wallet.retrieveTransactions()

                } catch (e: OperationFailedException) {
                    scheduler.post {
                        callbackWithError(ERROR_TRANSACTION_FAILED)
                    }
                    analytics.logEvent(
                            Events.Business.KINTransactionFailed(e.message, amount, TYPE_P2P))
                } catch (e: Exception) {
                    scheduler.post {
                        callbackWithError(ERROR_UPDATE_TRANSACTION_TO_SERVER)
                    }
                    analytics.logEvent(
                            Events.Business.KINTransactionFailed(e.message, amount, TYPE_P2P))
                }
            }

            private fun callbackWithError(e: Int) {
                scheduler.post {
                    callback.onError(e)
                }
            }
        })
    }
}