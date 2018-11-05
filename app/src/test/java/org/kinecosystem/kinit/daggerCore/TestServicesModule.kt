package org.kinecosystem.kinit.daggerCore

import android.content.Context
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.blockchain.Wallet
import org.kinecosystem.kinit.dagger.ServicesModule
import org.kinecosystem.kinit.repository.CategoriesRepository
import org.kinecosystem.kinit.repository.DataStoreProvider
import org.kinecosystem.kinit.repository.OffersRepository
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.server.*
import org.kinecosystem.kinit.util.Scheduler
import org.mockito.Mockito.mock

class TestServicesModule : ServicesModule() {

    override fun servicesProvider(context: Context, dataStoreProvider: DataStoreProvider, userRepository: UserRepository, offersRepository: OffersRepository, categoryRepository: CategoriesRepository, analytics: Analytics, scheduler: Scheduler): NetworkServices {
        return mock(NetworkServices::class.java)
    }

    override fun wallet(): Wallet {
        return mock(Wallet::class.java)
    }

    override fun onboardingService(): OnboardingService {
        return mock(OnboardingService::class.java)
    }

    override fun categoriesService(): CategoriesService {
        return mock(CategoriesService::class.java)
    }

    override fun taskService(): TaskService {
        return mock(TaskService::class.java)
    }

    override fun offerService(): OfferService {
        return mock(OfferService::class.java)
    }
}
