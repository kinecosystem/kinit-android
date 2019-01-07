package org.kinecosystem.tippic.server

import android.content.Context
import android.util.Log
import org.kinecosystem.tippic.model.Picture
import org.kinecosystem.tippic.repository.PictureRepository
import org.kinecosystem.tippic.repository.UserRepository
import org.kinecosystem.tippic.server.api.PictureApi
import org.kinecosystem.tippic.util.GeneralUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PictureService(private val pictureApi: PictureApi,val userRepo: UserRepository,val repository: PictureRepository) {
    fun refreshShownPicturesSummery(callback: OperationCompletionCallback? = null){
        pictureApi.picturesSummery(userRepo.userId()).enqueue(object : Callback<List<Picture>> {
            override fun onFailure(call: Call<List<Picture>>, t: Throwable) {
                callback?.onError(ERROR_APP_SERVER_FAILED_RESPONSE)
            }

            override fun onResponse(call: Call<List<Picture>>, response: Response<List<Picture>>?) {
                if (response != null && response.isSuccessful) {
                    Log.d("PictureService", "onResponse: ${response.body()}")
                    repository.updatePicturesSummery(response.body())
                    callback?.onSuccess()
                } else {
                    Log.d("PictureService", "response null or isSuccessful=false: $response")
                    callback?.onError(ERROR_EMPTY_RESPONSE)
                }
            }
        })
    }

    fun refreshPicture(callback: OperationCompletionCallback? = null) {
        pictureApi.picture(userRepo.userId()).enqueue(object : Callback<PictureApi.PictureResponds> {
            override fun onFailure(call: Call<PictureApi.PictureResponds>, t: Throwable) {
                callback?.onError(ERROR_APP_SERVER_FAILED_RESPONSE)
            }

            override fun onResponse(call: Call<PictureApi.PictureResponds>, response: Response<PictureApi.PictureResponds>?) {
                if (response != null && response.isSuccessful) {
                    Log.d("PictureService", "onResponse: ${response.body()}")
                    repository.updatePicture(response.body()?.picture)
                    callback?.onSuccess()
                } else {
                    Log.d("PictureService", "response null or isSuccessful=false: $response")
                    callback?.onError(ERROR_EMPTY_RESPONSE)
                }

            }
        })
    }
}