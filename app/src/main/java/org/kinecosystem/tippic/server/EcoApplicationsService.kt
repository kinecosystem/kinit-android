package org.kinecosystem.tippic.server

import android.content.Context
import org.kinecosystem.tippic.model.spend.preload
import org.kinecosystem.tippic.repository.EcoApplicationsRepository
import org.kinecosystem.tippic.repository.UserRepository
import org.kinecosystem.tippic.server.api.EcoApplicationsApi
import org.kinecosystem.tippic.util.GeneralUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EcoApplicationsService(context: Context, private val api: EcoApplicationsApi, private val repo: EcoApplicationsRepository, private val userRepository: UserRepository) {

    val applicationContext: Context = context.applicationContext

    fun retrieveApps(callback: OperationCompletionCallback? = null) {

        if (!GeneralUtils.isConnected(applicationContext)) {
            return
        }

        api.getApps(userRepository.userId()).enqueue(object : Callback<List<EcoApplicationsApi.AppsCategory>> {
            override fun onFailure(call: Call<List<EcoApplicationsApi.AppsCategory>>, t: Throwable) {
                callback?.onError(0)
            }

            override fun onResponse(call: Call<List<EcoApplicationsApi.AppsCategory>>, response: Response<List<EcoApplicationsApi.AppsCategory>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        repo.updateCategories(it)
                        for (appCategory in it) {
                            for (app in appCategory.apps) {
                                app.preload(applicationContext)
                            }
                        }
                    }

                }
                callback?.onSuccess()
            }
        })
    }
}