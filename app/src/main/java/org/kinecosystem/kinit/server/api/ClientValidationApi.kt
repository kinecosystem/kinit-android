package org.kinecosystem.kinit.server.api

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ClientValidationApi {
    data class ValidationNonceResponse(@SerializedName("nonce") val nonce: String?)
    data class ValidationStatus(@SerializedName("status") val status: String?)

    @GET("/validation/get-nonce")
    fun getNonce(@Header(USER_HEADER_KEY) userId: String): Call<ValidationNonceResponse>

    @POST("validation/validate-token")
    fun validateToken(@Header(USER_HEADER_KEY) userId: String, @Body token: String): Call<ValidationStatus>
}