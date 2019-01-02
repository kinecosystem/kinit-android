package org.kinecosystem.tippic.server.api

import org.kinecosystem.tippic.model.Picture
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface PictureApi {

    @GET("user/picture")
    fun getPicture(@Header(USER_HEADER_KEY) userId: String): Call<Picture>

    @GET("user/pictures-summery")
    fun getPicturesSummery(@Header(USER_HEADER_KEY) userId: String): Call<List<Picture>>


}