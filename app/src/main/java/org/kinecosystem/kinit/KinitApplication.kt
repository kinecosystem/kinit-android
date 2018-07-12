package org.kinecosystem.kinit

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.squareup.picasso.LruCache
import com.squareup.picasso.Picasso
import io.fabric.sdk.android.Fabric
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.dagger.*
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

    val CACHE_SIZE = 1024 * 7 // 7MB

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
        coreComponent = DaggerCoreComponent.builder().contextModule(
                ContextModule(applicationContext))
                .dataStoreProviderModule(DataStoreProviderModule(this))
                .userRepositoryModule(UserRepositoryModule())
                .tasksRepositoryModule(TasksRepositoryModule())
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

        val picasso = Picasso.Builder(this).memoryCache(LruCache(CACHE_SIZE)).build()
        Picasso.setSingletonInstance(picasso)
    }

    override fun dataStore(storage: String): DataStore {
        return SharedPreferencesStore(this, storage)
    }
}