package org.kinecosystem.kinit.network

import android.content.Context
import android.util.Log
import kin.core.exception.OperationFailedException
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.spend.Offer
import org.kinecosystem.kinit.model.spend.isValid
import org.kinecosystem.kinit.repository.OffersRepository
import org.kinecosystem.kinit.util.Scheduler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

const val ERROR_TRANSACTION_FAILED = 100
const val ERROR_REDEEM_COUPON_FAILED = 101
const val ERROR_NO_GOOD_LEFT = 200

class OfferService(context: Context, private val offersApi: OffersApi, val userId: String,
                   val repository: OffersRepository, val analytics: Analytics, val wallet: Wallet, val scheduler: Scheduler) {

    val applicationContext: Context = context.applicationContext


    fun retrieveOffers() {

        if (!NetworkUtils.isConnected(applicationContext)) {
            return
        }

        offersApi.offers(userId).enqueue(object : Callback<OffersApi.OffersResponse> {
            override fun onResponse(call: Call<OffersApi.OffersResponse>?,
                                    response: Response<OffersApi.OffersResponse>?) {

                if (response != null && response.isSuccessful) {
                    Log.d("OffersService", "onResponse: ${response.body()}")
                    val offerResponse = response.body()
                    if (offerResponse?.offerList != null && offerResponse.offerList.isNotEmpty()) {
                        repository.replaceOfferList(offerResponse.offerList)

                    } else {
                        Log.d("OffersService", "offer list empty or null ")
                        repository.replaceOfferList(ArrayList())
                    }
                } else {
                    Log.d("OffersService", "response null or isSuccessful=false: $response")
                }
            }

            override fun onFailure(call: Call<OffersApi.OffersResponse>?, t: Throwable?) {
                Log.d("TaskService", "onFailure called with throwable $t")
            }
        })
    }

    fun buyOffer(offer: Offer, callback: OperationResultCallback<String>) {

        if (!NetworkUtils.isConnected(applicationContext)) {
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
                    response = offersApi.bookOffer(userId,
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
                    val transactionId = wallet.payForOrder(offer.address!!, offer.price!!, bookOfferResponse?.orderId!!)

                    wallet.updateBalanceSync()
                    wallet.logSpendTransactionCompleted(offer.price, bookOfferResponse.orderId)

                    val response2 = offersApi.redeemOffer(userId,
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

                    wallet.retrieveCoupons()
                    wallet.retrieveTransactions()

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
                            offer.price?.toFloat(), "spend"))
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

}