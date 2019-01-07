package org.kinecosystem.tippic.server.api

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApi {
    data class BlockedUser(@SerializedName("user_id") val user_id: String, @SerializedName("username") val username: String)

    @POST("/user/block")
    fun blockUser(@Header(USER_HEADER_KEY) userId: String, @Body userIdToBlock: String): Call<OnboardingApi.StatusResponse>

    @POST("/user/unblock")
    fun unblockUser(@Header(USER_HEADER_KEY) userId: String, @Body userIdToUnblock: String): Call<OnboardingApi.StatusResponse>

    @GET("/user/block-list")
    fun getUserBlockList(): Call<List<BlockedUser>>
}