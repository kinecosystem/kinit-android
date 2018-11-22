package org.kinecosystem.kinit.server

import android.content.Context
import org.kinecosystem.kinit.model.earn.preload
import org.kinecosystem.kinit.repository.CategoriesRepository
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.server.api.CategoriesApi
import org.kinecosystem.kinit.util.GeneralUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoriesService(private val context: Context, private val api: CategoriesApi, private val userRepository: UserRepository, private val categoryRepository: CategoriesRepository) {

    fun retrieveCategories(callback: OperationCompletionCallback? = null) {
        if (!GeneralUtils.isConnected(context)) {
            return
        }
        api.categories(userRepository.userId()).enqueue(object : Callback<CategoriesApi.CategoriesResponse> {
            override fun onResponse(call: Call<CategoriesApi.CategoriesResponse>, response: Response<CategoriesApi.CategoriesResponse>) {
                if (response != null && response.isSuccessful && response.body() != null) {
                    response.body()?.let {
                        categoryRepository.updateCategories(it.categories)
                        //preload images
                        for (category in it.categories) {
                            category.preload(context.applicationContext)
                        }
                        categoryRepository.updateHeader(it.headerMessage)
                        callback?.onSuccess()

                    }
                } else {
                    callback?.onError(ERROR_APP_SERVER_FAILED_RESPONSE)
                }
            }

            override fun onFailure(call: Call<CategoriesApi.CategoriesResponse>, t: Throwable) {
                callback?.onError(ERROR_APP_SERVER_FAILED_RESPONSE)
            }

        })
    }
}


