package org.kinecosystem.kinit.server.api

import com.google.gson.annotations.SerializedName
import org.kinecosystem.kinit.model.spend.EcoApplication
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface EcoApplicationsApi {

    @GET("/app_discovery")
    fun getApps(@Header(USER_HEADER_KEY) userId: String): Call<List<AppsCategory>>

    data class AppsCategory(
            @SerializedName("category_name") val title: String,
            @SerializedName("category_id") val id: Int,
            @SerializedName("apps") val apps: List<EcoApplication>)






}