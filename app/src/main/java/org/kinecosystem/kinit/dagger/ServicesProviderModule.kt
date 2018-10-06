package org.kinecosystem.kinit.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.annotations.DebugOpenClass
import org.kinecosystem.kinit.blockchain.Wallet
import org.kinecosystem.kinit.repository.DataStoreProvider
import org.kinecosystem.kinit.repository.OffersRepository
import org.kinecosystem.kinit.repository.TasksRepository
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.server.OfferService
import org.kinecosystem.kinit.server.OnboardingService
import org.kinecosystem.kinit.server.ServicesProvider
import org.kinecosystem.kinit.server.TaskService
import org.kinecosystem.kinit.util.Scheduler
import javax.inject.Singleton

@DebugOpenClass
@Module(
        includes = [(ContextModule::class), (UserRepositoryModule::class), (TasksRepositoryModule::class), (OffersRepositoryModule::class), (AnalyticsModule::class), (DataStoreProviderModule::class)])
class ServicesProviderModule {

    lateinit var serivce: ServicesProvider
    @Provides
    @Singleton
    fun servicesProvider(context: Context, dataStoreProvider: DataStoreProvider,
                         userRepository: UserRepository, tasksRepo: TasksRepository,
                         offersRepository: OffersRepository, analytics: Analytics,
                         scheduler: Scheduler): ServicesProvider {
        serivce = ServicesProvider(context, dataStoreProvider,
                userRepository, tasksRepo, offersRepository, analytics, scheduler)
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

    @Provides
    @Singleton
    fun taskSerivce(): TaskService {
        return serivce.taskService
    }

    @Provides
    @Singleton
    fun offerService(): OfferService {
        return serivce.offerService
    }
}
