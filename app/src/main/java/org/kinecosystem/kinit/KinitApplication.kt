package org.kinecosystem.kinit

import android.app.Application
import com.crashlytics.android.Crashlytics
import dagger.*
import io.fabric.sdk.android.Fabric
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.network.ServicesProvider
import org.kinecosystem.kinit.network.Wallet
import org.kinecosystem.kinit.repository.DataStore
import org.kinecosystem.kinit.repository.DataStoreProvider
import org.kinecosystem.kinit.repository.SharedPreferencesStore
import org.kinecosystem.kinit.repository.UserRepository
import javax.inject.Inject


class KinitApplication : Application(), DataStoreProvider {

    companion object {
        @JvmStatic
        lateinit var coreComponent: CoreComponent
    }

    @Inject
    lateinit var analytics: Analytics
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var servicesProvider: ServicesProvider
    @Inject
    lateinit var wallet: Wallet

    override fun onCreate() {
        super.onCreate()
        Fabric.with(applicationContext, Crashlytics())
        coreComponent = DaggerCoreComponent.builder().contextModule(ContextModule(applicationContext))
            .dataStoreProviderModule(DataStoreProviderModule(this))
            .userRepositoryModule(UserRepositoryModule())
            .questionnaireRepositoryModule(QuestionnaireRepositoryModule())
            .offersRepositoryModule(OffersRepositoryModule())
            .analyticsModule(AnalyticsModule())
            .notificationModule(NotificationModule())
            .servicesProviderModule(ServicesProviderModule())
            .build()

        coreComponent.inject(this)
        analytics.init(this, userRepository.isFreshInstall)
        analytics.setUserId(userRepository.userId())
        servicesProvider.onBoardingService.appLaunch()
        servicesProvider.offerService.retrieveOffers()
        wallet.retrieveTransactions()
        wallet.retrieveCoupons()
        userRepository.isFreshInstall = false
    }

    override fun dataStore(storage: String): DataStore {
        return SharedPreferencesStore(this, storage)
    }
}