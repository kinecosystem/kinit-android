package org.kinecosystem.kinit.server.api

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ClientValidationApi {
    data class ValidationNonceResponse(@SerializedName("nonce") val nonce: String?)

    @GET("/validation/get-nonce")
    fun getNonce(@Header(USER_HEADER_KEY) userId: String): Call<ValidationNonceResponse>
}