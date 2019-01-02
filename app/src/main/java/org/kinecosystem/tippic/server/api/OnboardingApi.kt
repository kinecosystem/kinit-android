package org.kinecosystem.tippic.server.api

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface OnboardingApi {

    data class RegistrationInfo(@SerializedName("user_id") val userId: String,
                                @SerializedName("os") val os: String = "android",
                                @SerializedName("device_model") val deviceModel: String,
                                @SerializedName("time_zone") val timeZone: String,
                                @SerializedName("device_id") val deviceId: String,
                                @SerializedName("app_ver") val appVersion: String,
                                @SerializedName("screen_w") val screenWidth: Int,
                                @SerializedName("screen_h") val screenHeight: Int,
                                @SerializedName("density") val density: Int,
                                @SerializedName("package_id") val packageId: String)


    data class Config(@SerializedName("auth_token_enabled") val auth_token_enabled: Boolean,
                      @SerializedName("p2p_enabled") val p2p_enabled: Boolean,
                      @SerializedName("p2p_max_kin") val p2p_max_kin: Int,
                      @SerializedName("is_update_available") val is_update_available: Boolean,
                      @SerializedName("force_update") val force_update: Boolean,
                      @SerializedName("p2p_min_kin") val p2p_min_kin: Int,
                      @SerializedName("backup_nag") val backupNagEnabled: Boolean,
                      @SerializedName("p2p_min_tasks") val p2p_min_tasks: Int,
                      @SerializedName("phone_verification_enabled") val phone_verification_enabled: Boolean,
                      @SerializedName("faq_url") val faq_url: String,
                      @SerializedName("coming_soon") val coming_soon: String,
                      @SerializedName("tos") val tos: String)

    data class StatusResponse(@SerializedName("status") val status: String,
                              @SerializedName("config") val config: Config)

    data class HintsResponse(@SerializedName("status") val status: String,
                             @SerializedName("hints") val hints: ArrayList<String>)

    data class BlackListAreaCode(@SerializedName("areacodes") val list: List<String>)

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


    @POST("/user/auth/ack")
    fun authTokenAck(@Header(USER_HEADER_KEY) userId: String, @Body tokenInfo: TokenInfo): Call<StatusResponse>

    @GET("/blacklist/areacodes")
    fun blacklistAreaCodes(): Call<BlackListAreaCode>

    @POST("/user/restore")
    fun restoreAccount(@Header(USER_HEADER_KEY) userId: String, @Body accountAddress: AccountAddress): Call<AccountAddressResponds>

    data class AccountAddress(@SerializedName("address") val publicAddress: String)

    data class AccountAddressResponds(@SerializedName("status") val status: String,
                                      @SerializedName("user_id") val userId: String)

}
