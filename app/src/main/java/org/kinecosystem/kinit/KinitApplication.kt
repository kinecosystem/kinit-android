package org.kinecosystem.kinit

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.squareup.picasso.Picasso
import io.fabric.sdk.android.Fabric
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.dagger.*
import org.kinecosystem.kinit.repository.DataStore
import org.kinecosystem.kinit.repository.DataStoreProvider
import org.kinecosystem.kinit.repository.SharedPreferencesStore
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.server.NetworkServices
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
    lateinit var networkServices: NetworkServices

    override fun onCreate() {
        super.onCreate()
        Fabric.with(applicationContext, Crashlytics())
        coreComponent = DaggerCoreComponent.builder().contextModule(
                ContextModule(applicationContext))
                .dataStoreModule(DataStoreModule(this))
                .userRepositoryModule(UserRepositoryModule())
                .ecoAppsRepositoryModule(EcoAppsRepositoryModule())
                .offersRepositoryModule(OffersRepositoryModule())
                .schedulerModule(SchedulerModule())
                .analyticsModule(AnalyticsModule())
                .notificationModule(NotificationModule())
                .servicesModule(ServicesModule())
                .build()

        coreComponent.inject(this)
        analytics.init(this, userRepository.isFreshInstall)
        analytics.setUserId(userRepository.userId())
        networkServices.onBoardingService.appLaunch()
        networkServices.backupService.retrieveHints()
        networkServices.offerService.retrieveOffers()
        networkServices.categoriesService.retrieveCategories()
        networkServices.taskService.retrieveAllTasks()
        networkServices.ecoApplicationServie.retrieveApps()
        networkServices.walletService.retrieveTransactions()
        networkServices.walletService.retrieveCoupons()
        userRepository.isFreshInstall = false

        val picasso = Picasso.Builder(this).build()
        Picasso.setSingletonInstance(picasso)
    }

    override fun dataStore(storage: String): DataStore {
        return SharedPreferencesStore(this, storage)
    }
}