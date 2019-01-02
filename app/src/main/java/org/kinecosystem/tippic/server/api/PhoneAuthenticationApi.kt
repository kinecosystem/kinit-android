package org.kinecosystem.tippic.server.api

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PhoneAuthenticationApi {

    data class AuthInfo(@SerializedName("token") val token: String)

    @POST("/user/firebase/update-id-token")
    fun updatePhoneAuthToken(@Header(
            USER_HEADER_KEY) userId: String, @Body authInfo: AuthInfo): Call<OnboardingApi.HintsResponse>
}