package org.kinecosystem.tippic.server.api

import com.google.gson.annotations.SerializedName
import org.kinecosystem.tippic.model.Picture
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface PictureApi {

    data class PictureResponds(@SerializedName("picture") val picture: Picture?)

    @GET("/user/picture")
    fun picture(@Header(USER_HEADER_KEY) userId: String): Call<PictureResponds>

    @GET(".user/pictures-summery")
    fun picturesSummery(@Header(USER_HEADER_KEY) userId: String): Call<List<Picture>>


}