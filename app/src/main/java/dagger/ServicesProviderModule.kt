package dagger

import android.content.Context
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.network.*
import org.kinecosystem.kinit.repository.DataStoreProvider
import org.kinecosystem.kinit.repository.OffersRepository
import org.kinecosystem.kinit.repository.QuestionnaireRepository
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.util.Scheduler
import javax.inject.Singleton

@Module(
    includes = [(ContextModule::class), (UserRepositoryModule::class), (QuestionnaireRepositoryModule::class), (OffersRepositoryModule::class), (AnalyticsModule::class), (AnalyticsModule::class), (DataStoreProviderModule::class)])
class ServicesProviderModule {

    lateinit var serivce: ServicesProvider
    @Provides
    @Singleton
    fun servicesProvider(context: Context, dataStoreProvider: DataStoreProvider,
        userRepository: UserRepository, questionnaireRepo: QuestionnaireRepository,
        offersRepository: OffersRepository, analytics: Analytics,
        scheduler: Scheduler): ServicesProvider {
        serivce = ServicesProvider(context, dataStoreProvider,
            userRepository, questionnaireRepo, offersRepository, analytics, scheduler)
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
