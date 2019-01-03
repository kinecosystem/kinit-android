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

class PictureService(context: Context,private val pictureApi: PictureApi,val userRepo: UserRepository,val repository: PictureRepository) {
    private val applicationContext: Context = context.applicationContext

    fun retrievePicture(callback: OperationCompletionCallback? = null) {
        if (!GeneralUtils.isConnected(applicationContext)) return

        pictureApi.picture(userRepo.userId()).enqueue(object : Callback<PictureApi.PictureResponds> {
            override fun onFailure(call: Call<PictureApi.PictureResponds>, t: Throwable) {
                callback?.onError(0)
            }

            override fun onResponse(call: Call<PictureApi.PictureResponds>, response: Response<PictureApi.PictureResponds>?) {
                if (response != null && response.isSuccessful) {
                    Log.d("PictureService", "onResponse: ${response.body()}")
                    response.body()?.let {
                        repository.updatePicture(it.picture)
                    }
                    callback?.onSuccess()
                } else {
                    Log.d("PictureService", "response null or isSuccessful=false: $response")
                    callback?.onError(ERROR_EMPTY_RESPONSE)
                }

            }
        })
    }
}