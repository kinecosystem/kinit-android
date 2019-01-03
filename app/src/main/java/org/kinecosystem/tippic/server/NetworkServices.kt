package org.kinecosystem.tippic.server

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kinecosystem.tippic.BuildConfig
import org.kinecosystem.tippic.analytics.Analytics
import org.kinecosystem.tippic.blockchain.Wallet
import org.kinecosystem.tippic.repository.*
import org.kinecosystem.tippic.server.api.*
import org.kinecosystem.tippic.util.GeneralUtils
import org.kinecosystem.tippic.util.Scheduler
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val STAGE_BASE_URL = BuildConfig.StageBaseUrl
private const val PROD_BASE_URL = BuildConfig.ProdBaseUrl

interface OperationCompletionCallback {
    fun onSuccess()
    fun onError(errorCode: Int)
}

interface OperationResultCallback<in T> {
    fun onResult(result: T)
    fun onError(errorCode: Int)
}

class NetworkServices {
    val onBoardingService: OnboardingService
    val pictureService: PictureService
    val walletService: Wallet

    private val applicationContext: Context

    constructor(context: Context, dataStoreProvider: DataStoreProvider, userRepo: UserRepository, pictureRepository: PictureRepository, analytics: Analytics, scheduler: Scheduler
    ) {
        applicationContext = context.applicationContext

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        val client = OkHttpClient.Builder().readTimeout(20, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build()

        val baseUrl = if (BuildConfig.DEBUG) STAGE_BASE_URL else PROD_BASE_URL
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val onboardingApi = retrofit.create<OnboardingApi>(OnboardingApi::class.java)
        val walletApi = retrofit.create<WalletApi>(WalletApi::class.java)


        walletService = Wallet(context.applicationContext, dataStoreProvider, userRepo, analytics,
                onboardingApi, walletApi, scheduler)

        pictureService = PictureService(applicationContext,retrofit.create<PictureApi>(PictureApi::class.java),userRepo, pictureRepository)
        onBoardingService = OnboardingService(applicationContext,
                retrofit.create<OnboardingApi>(OnboardingApi::class.java),
                retrofit.create<PhoneAuthenticationApi>(PhoneAuthenticationApi::class.java),
                userRepo, analytics, walletService,pictureService )

    }

    fun isNetworkConnected(): Boolean {
        return GeneralUtils.isConnected(applicationContext)
    }
}
