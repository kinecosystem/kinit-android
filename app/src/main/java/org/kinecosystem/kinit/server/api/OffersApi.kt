package org.kinecosystem.kinit.server.api

import com.google.gson.annotations.SerializedName
import org.kinecosystem.kinit.model.spend.Offer
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface OffersApi {

    // Retrieve list of offers
    data class OffersResponse(@SerializedName("offers") val offerList: List<Offer>)

    @GET("/user/offers")
    fun offers(@Header(USER_HEADER_KEY) userId: String): Call<OffersResponse>

    // book an offer
    data class OfferInfo(@SerializedName("id") val id: String, @SerializedName("validation_token") val token: String?)

    data class BookOfferResponse(@SerializedName("status") val status: String, @SerializedName(
        "reason") val reason: String, @SerializedName(
        "order_id") val orderId: String)


    data class ContactResponse(
        @SerializedName("status") val status: String,
        @SerializedName("reason") val reason: String,
        @SerializedName("address") val address: String)


    data class TransactionInfo(
        @SerializedName("tx_hash") val txHash: String,
        @SerializedName("destination_address") val address: String,
        @SerializedName("amount") val amount: Int)


    data class StatusResponse(@SerializedName("status") val status: String)

    data class ContactInfo(
        @SerializedName("phone_number") val phoneNumber: String)

    @POST("/offer/book")
    fun bookOffer(@Header(USER_HEADER_KEY) userId: String, @Body offerInfo: OfferInfo): Call<BookOfferResponse>


    @POST("/user/contact")
    fun sendContact(@Header(USER_HEADER_KEY) userId: String, @Body contactInfo: ContactInfo): Call<ContactResponse>

    @POST("/user/transaction/p2p")
    fun sendTransactionInfo(@Header(
            USER_HEADER_KEY) userId: String, @Body transactionInfo: TransactionInfo): Call<StatusResponse>

    // redeem coupon after paying
    data class PaymentReceipt(@SerializedName("tx_hash") val txHash: String)

    data class Good(@SerializedName("type") val type: String, @SerializedName("value") val value: String)

    data class RedeemResponse(@SerializedName("goods") val goods: List<Good>, @SerializedName(
        "status") val status: String)

    @POST("/offer/redeem")
    fun redeemOffer(@Header(USER_HEADER_KEY) userId: String, @Body paymentReceipt: PaymentReceipt): Call<RedeemResponse>
}