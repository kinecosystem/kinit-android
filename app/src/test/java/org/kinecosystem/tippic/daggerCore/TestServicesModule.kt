package org.kinecosystem.tippic.daggerCore

import android.content.Context
import org.kinecosystem.tippic.analytics.Analytics
import org.kinecosystem.tippic.blockchain.WalletService
import org.kinecosystem.tippic.dagger.ServicesModule
import org.kinecosystem.tippic.repository.*
import org.kinecosystem.tippic.server.*
import org.kinecosystem.tippic.util.Scheduler
import org.mockito.Mockito.mock

class TestServicesModule : ServicesModule() {

    override fun servicesProvider(context: Context, dataStoreProvider: DataStoreProvider, userRepository: UserRepository,  analytics: Analytics, scheduler: Scheduler): NetworkServices {
        return mock(NetworkServices::class.java)
    }

    override fun walletService(): WalletService {
        return mock(WalletService::class.java)
    }

    override fun onboardingService(): OnboardingService {
        return mock(OnboardingService::class.java)
    }

}
