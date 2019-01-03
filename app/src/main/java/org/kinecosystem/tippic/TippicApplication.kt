package org.kinecosystem.tippic

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.squareup.picasso.Picasso
import io.fabric.sdk.android.Fabric
import org.kinecosystem.tippic.analytics.Analytics
import org.kinecosystem.tippic.dagger.*
import org.kinecosystem.tippic.repository.DataStore
import org.kinecosystem.tippic.repository.DataStoreProvider
import org.kinecosystem.tippic.repository.SharedPreferencesStore
import org.kinecosystem.tippic.repository.UserRepository
import org.kinecosystem.tippic.server.NetworkServices
import javax.inject.Inject


class TippicApplication : Application(), DataStoreProvider {

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
                .pictureRepositoryModule(PictureRepositoryModule())
                .schedulerModule(SchedulerModule())
                .analyticsModule(AnalyticsModule())
                .notificationModule(NotificationModule())
                .servicesModule(ServicesModule())
                .build()

        coreComponent.inject(this)
        analytics.init(this, userRepository.isFreshInstall)
        analytics.setUserId(userRepository.userId())
        networkServices.onBoardingService.appLaunch()
        userRepository.isFreshInstall = false

        val picasso = Picasso.Builder(this).build()
        Picasso.setSingletonInstance(picasso)
    }

    override fun dataStore(storage: String): DataStore {
        return SharedPreferencesStore(this, storage)
    }
}