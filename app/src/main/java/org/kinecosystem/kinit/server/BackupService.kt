package org.kinecosystem.kinit.server

import android.content.Context
import android.util.Log
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.server.api.BackupApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BackupService(val applicationContext: Context, val userRepo: UserRepository, val server: BackupApi) {


    fun initHints() {
        server.getHints().enqueue(object : Callback<BackupApi.HintsResponse> {
            override fun onResponse(call: Call<BackupApi.HintsResponse>?, response: Response<BackupApi.HintsResponse>?) {
                if (response != null && response.isSuccessful) {
                    response.body()?.let {
                        userRepo.backUpHints = it.hints
                    }
                }
            }

            override fun onFailure(call: Call<BackupApi.HintsResponse>?, t: Throwable?) {
                Log.e("BackupService", "getHints failed")
                userRepo.backUpHints = listOf()
            }

        })
    }

    fun updateHints(hintsIds: List<Int>) {
        server.updateHints(userRepo.userId(), userRepo.authToken, BackupApi.Hints(hintsIds)).enqueue(object : Callback<BackupApi.StatusResponse> {
            override fun onFailure(call: Call<BackupApi.StatusResponse>?, t: Throwable?) {
                //TODO
            }

            override fun onResponse(call: Call<BackupApi.StatusResponse>?, response: Response<BackupApi.StatusResponse>?) {
            }

        })
    }

    fun updateBackupDataTo(emailAddress: String, key: String) {
        server.sendBackUpEmail(userRepo.userId(), userRepo.authToken, BackupApi.BackupData(emailAddress, key)).enqueue(object : Callback<BackupApi.StatusResponse> {
            override fun onFailure(call: Call<BackupApi.StatusResponse>?, t: Throwable?) {
                //TODO
            }

            override fun onResponse(call: Call<BackupApi.StatusResponse>?, response: Response<BackupApi.StatusResponse>?) {
            }

        })
    }

}