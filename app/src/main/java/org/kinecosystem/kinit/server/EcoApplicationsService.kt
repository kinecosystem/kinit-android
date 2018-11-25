package org.kinecosystem.kinit.server

import android.content.Context
import android.util.Log
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.repository.EcoApplicationsRepository
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.server.api.EcoApplicationsApi
import org.kinecosystem.kinit.util.GeneralUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EcoApplicationsService(context: Context, private val api: EcoApplicationsApi,private val repo: EcoApplicationsRepository, private val userRepository: UserRepository,
                            private val analytics: Analytics) {

    val applicationContext: Context = context.applicationContext

    fun retrieveApps(callback: OperationCompletionCallback? = null) {

        if (!GeneralUtils.isConnected(applicationContext)) {
            return
        }

        api.getApps(userRepository.userId()).enqueue(object : Callback<List<EcoApplicationsApi.AppsCategory>> {
            override fun onFailure(call: Call<List<EcoApplicationsApi.AppsCategory>>, t: Throwable) {
                Log.d("####", "### no apps ")
                //repo.updateCategories(listOf(EcoApplicationsApi.AppsCategory("ttt", 1, listOf(EcoApplication("iddd", "whaap", null, EcoApplicationData("ddd", "", "", "fun and more", "", "", null, null, "usaage", "fffdrr"))))))
            }

            override fun onResponse(call: Call<List<EcoApplicationsApi.AppsCategory>>, response: Response<List<EcoApplicationsApi.AppsCategory>>) {
                if(response.isSuccessful) {
                    repo.updateCategories(response.body())
                }
            }

        })
    }


}