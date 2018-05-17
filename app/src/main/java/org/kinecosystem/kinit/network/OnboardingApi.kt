package org.kinecosystem.kinit.network

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OnboardingApi {

    data class RegistrationInfo(@SerializedName("user_id") val userId: String,
        @SerializedName("os") val os: String = "android",
        @SerializedName("device_model") val deviceModel: String,
        @SerializedName("time_zone") val timeZone: String,
        @SerializedName("device_id") val deviceId: String,
        @SerializedName("app_ver") val appVersion: String)


    data class Config(@SerializedName("auth_token_enabled") val auth_token_enabled: Boolean,
        @SerializedName("p2p_enabled") val p2p_enabled: Boolean,
        @SerializedName("p2p_max_kin") val p2p_max_kin: String,
        @SerializedName("p2p_min_kin") val p2p_min_kin: String,
        @SerializedName("phone_verification_enabled") val phone_verification_enabled: Boolean,
        @SerializedName("tos") val tos: String)

    data class StatusResponse(@SerializedName("status") val status: String,
        @SerializedName("config") val config: Config)

    @POST("/user/register")
    fun register(@Body registrationInfo: RegistrationInfo): Call<StatusResponse>

    data class AppVersion(@SerializedName("app_ver") val appVersion: String)

    @POST("/user/app-launch")
    fun appLaunch(@Header(USER_HEADER_KEY) userId: String, @Body appVersionBody: AppVersion): Call<StatusResponse>

    data class TokenInfo(@SerializedName("token") val token: String)

    data class AccountInfo(@SerializedName("public_address") val publicAddress: String)

    @POST("/user/update-token")
    fun updateToken(@Header(USER_HEADER_KEY) userId: String, @Body tokenInfo: TokenInfo): Call<StatusResponse>

    @POST("/user/onboard")
    fun createAccount(@Header(USER_HEADER_KEY) userId: String, @Body accountInfo: AccountInfo): Call<StatusResponse>


}