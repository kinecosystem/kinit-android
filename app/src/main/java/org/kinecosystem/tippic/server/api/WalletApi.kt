package org.kinecosystem.tippic.server.api

import com.google.gson.annotations.SerializedName
import org.kinecosystem.tippic.model.TippicTransaction
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface WalletApi {

    // Retrieve list of transactions
    data class TransactionsResponse(
        @SerializedName("status") val status: String?,
        @SerializedName("txs") val txs: List<TippicTransaction>?
    )

    @GET("user/transactions")
    fun getTransactions(@Header(USER_HEADER_KEY) userId: String): Call<TransactionsResponse>

    @POST("/user/transaction/report")
    fun reportTransaction(@Header(USER_HEADER_KEY) userId: String, @Body tx: TippicTransaction): Call<OnboardingApi.StatusResponse>
}