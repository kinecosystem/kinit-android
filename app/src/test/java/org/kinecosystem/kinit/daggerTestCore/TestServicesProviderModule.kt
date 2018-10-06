package org.kinecosystem.kinit.daggerTestCore

import android.content.Context
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.blockchain.Wallet
import org.kinecosystem.kinit.dagger.ServicesProviderModule
import org.kinecosystem.kinit.repository.DataStoreProvider
import org.kinecosystem.kinit.repository.OffersRepository
import org.kinecosystem.kinit.repository.TasksRepository
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.server.OfferService
import org.kinecosystem.kinit.server.OnboardingService
import org.kinecosystem.kinit.server.ServicesProvider
import org.kinecosystem.kinit.server.TaskService
import org.kinecosystem.kinit.util.Scheduler
import org.mockito.Mockito.mock

class TestServicesProviderModule : ServicesProviderModule() {

    override fun servicesProvider(context: Context, dataStoreProvider: DataStoreProvider, userRepository: UserRepository, tasksRepo: TasksRepository, offersRepository: OffersRepository, analytics: Analytics, scheduler: Scheduler): ServicesProvider {
        return mock(ServicesProvider::class.java)
    }

    override fun wallet(): Wallet {
        return mock(Wallet::class.java)
    }

    override fun onboardingService(): OnboardingService {
        return mock(OnboardingService::class.java)
    }

    override fun taskSerivce(): TaskService {
        return mock(TaskService::class.java)
    }

    override fun offerService(): OfferService {
        return mock(OfferService::class.java)
    }
}
