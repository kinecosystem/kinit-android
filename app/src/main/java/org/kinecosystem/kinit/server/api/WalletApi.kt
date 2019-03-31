package org.kinecosystem.kinit.server.api

import com.google.gson.annotations.SerializedName
import org.kinecosystem.kinit.model.KinTransaction
import org.kinecosystem.kinit.model.spend.Coupon
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface WalletApi {

    // Retrieve list of coupons
    data class CouponsResponse(
        @SerializedName("status") val status: String?,
        @SerializedName("redeemed") val coupons: ArrayList<Coupon>?
    )

    @GET("user/redeemed")
    fun getCoupons(@Header(USER_HEADER_KEY) userId: String): Call<CouponsResponse>

    // Retrieve list of transactions
    data class TransactionsResponse(
        @SerializedName("status") val status: String?,
        @SerializedName("txs") val txs: List<KinTransaction>?
    )

    @GET("user/transactions")
    fun getTransactions(@Header(USER_HEADER_KEY) userId: String): Call<TransactionsResponse>

    data class TransactionInfo(
        @SerializedName("id") val id: String,
        @SerializedName("sender_address") val senderAddress: String,
        @SerializedName("recipient_address") val recipientAddress: String,
        @SerializedName("amount") val amount: Int,
        @SerializedName("transaction") val transaction: String,
        @SerializedName("validation-token") val validationToken: String?)

    data class AddSignatureResponse(
        @SerializedName("status") val status: String,
        @SerializedName("tx") val signedTransaction: String)

    @POST("/user/add-signature")
    fun addSignature(@Header(USER_HEADER_KEY) userId: String, @Body whitelistInfo: TransactionInfo): Call<AddSignatureResponse>

}