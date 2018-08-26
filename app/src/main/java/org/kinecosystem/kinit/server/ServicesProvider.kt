package org.kinecosystem.kinit.server

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kinecosystem.kinit.BuildConfig
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.blockchain.Wallet
import org.kinecosystem.kinit.repository.DataStoreProvider
import org.kinecosystem.kinit.repository.OffersRepository
import org.kinecosystem.kinit.repository.TasksRepository
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.server.api.*
import org.kinecosystem.kinit.util.NetworkUtils
import org.kinecosystem.kinit.util.Scheduler
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

class ServicesProvider {
    val onBoardingService: OnboardingService
    val taskService: TaskService
    val walletService: Wallet
    val offerService: OfferService

    private val applicationContext: Context

    constructor(context: Context, dataStoreProvider: DataStoreProvider, userRepo: UserRepository,
                tasksRepo: TasksRepository, offerRepo: OffersRepository,
                analytics: Analytics, scheduler: Scheduler
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


        walletService = Wallet(context.applicationContext, dataStoreProvider, userRepo, tasksRepo, analytics,
                onboardingApi, walletApi, scheduler)
        taskService = TaskService(applicationContext,
                retrofit.create<TasksApi>(TasksApi::class.java),
                tasksRepo, userRepo.userId(), walletService)
        onBoardingService = OnboardingService(applicationContext,
                retrofit.create<OnboardingApi>(OnboardingApi::class.java),
                retrofit.create<PhoneAuthenticationApi>(PhoneAuthenticationApi::class.java),
                userRepo, analytics, taskService, walletService)
        offerService = OfferService(applicationContext, retrofit.create<OffersApi>(OffersApi::class.java),
                userRepo.userId(), offerRepo, analytics, walletService, scheduler)
    }

    fun isNetworkConnected(): Boolean {
        return NetworkUtils.isConnected(applicationContext)
    }
}
