package org.kinecosystem.kinit

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.network.ServicesProvider
import org.kinecosystem.kinit.notification.NotificationPublisher
import org.kinecosystem.kinit.repository.*
import org.kinecosystem.kinit.util.AndroidScheduler
import org.kinecosystem.kinit.util.Scheduler


class KinitApplication : Application(), CoreComponentsProvider {

    private lateinit var coreComponentsProvider: CoreComponentsProvider

    override fun onCreate() {
        super.onCreate()
        Fabric.with(applicationContext, Crashlytics())
        coreComponentsProvider = CoreComponentsProviderImpl(this, this)
        analytics().init(this, userRepo().isFreshInstall)
        analytics().setUserId(userRepo().userId())
        services().onBoardingService.appLaunch()
        services().offerService.retrieveOffers()
        userRepo().isFreshInstall = false
    }

    override fun userRepo(): UserRepository {
        return coreComponentsProvider.userRepo()
    }

    override fun questionnaireRepo(): QuestionnaireRepository {
        return coreComponentsProvider.questionnaireRepo()
    }

    override fun offersRepo(): OffersRepository {
        return coreComponentsProvider.offersRepo()
    }

    override fun services(): ServicesProvider {
        return coreComponentsProvider.services()
    }

    override fun analytics(): Analytics {
        return coreComponentsProvider.analytics()
    }

    override fun dataStore(storage: String): DataStore {
        return SharedPreferencesStore(this, storage)
    }

    override fun scheduler(): Scheduler {
        return AndroidScheduler()
    }

    override fun notificationPublisher(): NotificationPublisher {
        return coreComponentsProvider.notificationPublisher()
    }
}