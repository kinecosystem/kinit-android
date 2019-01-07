package org.kinecosystem.tippic.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import org.kinecosystem.tippic.analytics.Analytics
import org.kinecosystem.tippic.blockchain.WalletService
import org.kinecosystem.tippic.repository.*
import org.kinecosystem.tippic.server.*
import org.kinecosystem.tippic.util.Scheduler
import javax.inject.Singleton

@DebugOpenClass
@Module(
        includes = [(ContextModule::class), (UserRepositoryModule::class), (PictureRepositoryModule::class), (AnalyticsModule::class), (DataStoreModule::class)])
class ServicesModule {

    lateinit var serivce: NetworkServices
    @Provides
    @Singleton
    fun servicesProvider(context: Context, dataStoreProvider: DataStoreProvider,
                         userRepository: UserRepository,pictureRepository: PictureRepository, analytics: Analytics,
                         scheduler: Scheduler): NetworkServices {
        serivce = NetworkServices(context, dataStoreProvider,
                userRepository, pictureRepository, analytics, scheduler)
        return serivce
    }

    @Provides
    @Singleton
    fun pictureService(): PictureService {
        return serivce.pictureService
    }

    @Provides
    @Singleton
    fun walletService(): WalletService {
        return serivce.walletService
    }

    @Provides
    @Singleton
    fun onboardingService(): OnboardingService {
        return serivce.onBoardingService
    }

}
