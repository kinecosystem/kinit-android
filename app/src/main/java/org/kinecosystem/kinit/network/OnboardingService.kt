package org.kinecosystem.kinit.network

import android.content.Context
import android.util.Log
import org.kinecosystem.kinit.BuildConfig
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.analytics.Events.Business.UserRegistered
import org.kinecosystem.kinit.device.DeviceUtils
import org.kinecosystem.kinit.network.OnboardingApi.StatusResponse
import org.kinecosystem.kinit.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OnboardingService(context: Context, private val appLaunchApi: OnboardingApi,
    private val phoneAuthenticationApi: PhoneAuthenticationApi,
    val userRepo: UserRepository,
    val analytics: Analytics,
    val taskService: TaskService,
    val wallet: Wallet) {

    private val applicationContext: Context = context.applicationContext

    fun appLaunch() {
        val appVersion = BuildConfig.VERSION_NAME
        if (!NetworkUtils.isConnected(applicationContext))
            return
        Log.d("OnboardingService", "### appLaunch user registered = $userRepo.isRegistered")
        if (!userRepo.isRegistered) {
            callRegister(appVersion)
        } else {
            callAppLaunch(appVersion)
        }
    }

    fun sendPhoneAuthentication(
        phoneNumber: String, token: String, callback: Callback<StatusResponse>) {

        if (!NetworkUtils.isConnected(applicationContext)) {
            return
        }
        phoneAuthenticationApi.updatePhoneAuthToken(userRepo.userId(),
            PhoneAuthenticationApi.AuthInfo(phoneNumber, token)).enqueue(callback)
    }

    private fun updateToken() {
        if (userRepo.notSentFcmToken.isNotBlank()) {
            updateToken(userRepo.notSentFcmToken)
        }
    }

    fun updateToken(token: String) {
        //  saving it in case there is an error
        userRepo.notSentFcmToken = token

        val call = appLaunchApi.updateToken(userRepo.userId(), OnboardingApi.TokenInfo(token))
        call.enqueue(
            object : Callback<StatusResponse> {
                override fun onResponse(call: Call<StatusResponse>?,
                    response: Response<StatusResponse>?) {
                    Log.d("OnboardingService", "### updateToken success : $response token:$token")
                    // successfully sent the token update to server - so it is not longer needed
                    if (response != null && response.isSuccessful) {
                        // managed to send so can delete now
                        userRepo.notSentFcmToken = ""
                    }
                }

                override fun onFailure(call: Call<StatusResponse>?, t: Throwable?) {
                    Log.e("OnboardingService", "### updateToken onFailure called with throwable $t")
                }
            })
    }

    private fun callRegister(appVersion: String) {
        val deviceUtils = DeviceUtils(applicationContext)
        val userId: String = userRepo.userId()
        val timeZoneWithoutGMT = deviceUtils.timeZone().drop(3)
        deviceUtils.timeZoneDebugging(analytics)
        val call = appLaunchApi.register(OnboardingApi.RegistrationInfo(userId,
            deviceModel = deviceUtils.deviceName(),
            timeZone = timeZoneWithoutGMT,
            deviceId = deviceUtils.deviceId(),
            appVersion = appVersion))
        call.enqueue(object : Callback<StatusResponse> {
            override fun onResponse(call: Call<StatusResponse>?,
                response: Response<StatusResponse>?) {

                if (response != null && response.isSuccessful) {
                    updateConfig(response)
                    updateToken()
                    analytics.logEvent(UserRegistered())
                    userRepo.isRegistered = true
                    wallet.initKinWallet()
                    taskService.retrieveNextTask()
                } else {
                    Log.d("OnboardingService", "### register onResponse NOT SUCCESSFULL OR null: $response")
                    analytics.logEvent(Events.BILog.UserRegistrationFailed("response: $response"))
                }
            }

            override fun onFailure(call: Call<StatusResponse>?, t: Throwable?) {
                Log.d("OnboardingService", "### register onFailure called with throwable $t")
                analytics.logEvent(Events.BILog.UserRegistrationFailed("onFailure with throwable $t"))
            }
        })
    }

    private fun updateConfig(response: Response<StatusResponse>) {
        val config = response.body()?.config
        userRepo.tos = config?.tos ?: ""
        userRepo.isPhoneVerificationEnabled = config?.phone_verification_enabled ?: false
    }

    private fun callAppLaunch(appVersion: String) {
        updateToken()
        val call = appLaunchApi.appLaunch(userRepo.userId(), OnboardingApi.AppVersion(appVersion))
        call.enqueue(
            object : Callback<StatusResponse> {
                override fun onResponse(call: Call<StatusResponse>?,
                    response: Response<StatusResponse>?) {
                    if (response != null && response.isSuccessful) {
                        updateConfig(response)
                        wallet.initKinWallet()
                        Log.d("OnboardingService",
                            "appLaunch onResponse: $response" + " config " + response?.body()?.config)
                    }
                }

                override fun onFailure(call: Call<StatusResponse>?, t: Throwable?) {
                    Log.d("OnboardingService", "appLaunch onFailure called with throwable $t")
                }
            })
    }
}