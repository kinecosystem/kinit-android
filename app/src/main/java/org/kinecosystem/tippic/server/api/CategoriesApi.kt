package org.kinecosystem.tippic.server.api

import com.google.gson.annotations.SerializedName
import org.kinecosystem.tippic.model.earn.Category
import org.kinecosystem.tippic.model.earn.HeaderMessage
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface CategoriesApi {

    data class CategoriesResponse(
            @SerializedName("categories") val categories: List<Category>,
            @SerializedName("header_message") val headerMessage: HeaderMessage)


    @GET("/user/categories")
    fun categories(@Header(USER_HEADER_KEY) userId: String): Call<CategoriesResponse>
}

