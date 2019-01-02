package org.kinecosystem.tippic.server.api

import com.google.gson.annotations.SerializedName
import org.kinecosystem.tippic.model.KinTransaction
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface WalletApi {

    // Retrieve list of transactions
    data class TransactionsResponse(
        @SerializedName("status") val status: String?,
        @SerializedName("txs") val txs: List<KinTransaction>?
    )

    @GET("user/transactions")
    fun getTransactions(@Header(USER_HEADER_KEY) userId: String): Call<TransactionsResponse>
}