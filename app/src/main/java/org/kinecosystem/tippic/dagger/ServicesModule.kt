package org.kinecosystem.tippic.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import org.kinecosystem.tippic.analytics.Analytics
import org.kinecosystem.tippic.blockchain.Wallet
import org.kinecosystem.tippic.repository.*
import org.kinecosystem.tippic.server.*
import org.kinecosystem.tippic.util.Scheduler
import javax.inject.Singleton

@DebugOpenClass
@Module(
        includes = [(ContextModule::class), (UserRepositoryModule::class), (AnalyticsModule::class), (DataStoreModule::class)])
class ServicesModule {

    lateinit var serivce: NetworkServices
    @Provides
    @Singleton
    fun servicesProvider(context: Context, dataStoreProvider: DataStoreProvider,
                         userRepository: UserRepository, analytics: Analytics,
                         scheduler: Scheduler): NetworkServices {
        serivce = NetworkServices(context, dataStoreProvider,
                userRepository, analytics, scheduler)
        return serivce
    }

    @Provides
    @Singleton
    fun wallet(): Wallet {
        return serivce.walletService
    }

    @Provides
    @Singleton
    fun onboardingService(): OnboardingService {
        return serivce.onBoardingService
    }

}
