package org.kinecosystem.kinit

import android.content.Context
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.network.ServicesProvider
import org.kinecosystem.kinit.notification.NotificationPublisher
import org.kinecosystem.kinit.repository.*
import org.kinecosystem.kinit.util.AndroidScheduler
import org.kinecosystem.kinit.util.Scheduler

class CoreComponentsProviderImpl(context: Context, dataStoreProvider: DataStoreProvider) : CoreComponentsProvider {
    private val appContext: Context = context.applicationContext
    private val userRepository: UserRepository = UserRepository(dataStoreProvider)
    private val questionnaireRepo: QuestionnaireRepository = QuestionnaireRepository(dataStoreProvider)
    private val offersRepository: OffersRepository = OffersRepository()
    private val analytics = Analytics()
    private val androidScheduler = AndroidScheduler()
    private val serviceProvider = ServicesProvider(context.applicationContext, dataStoreProvider,
        userRepository, questionnaireRepo, offersRepository, analytics, androidScheduler)
    private val notificationPublisher = NotificationPublisher(appContext, analytics)

    override fun userRepo(): UserRepository {
        return userRepository
    }

    override fun questionnaireRepo(): QuestionnaireRepository {
        return questionnaireRepo
    }

    override fun offersRepo(): OffersRepository {
        return offersRepository;
    }

    override fun services(): ServicesProvider {
        return serviceProvider
    }

    override fun analytics(): Analytics {
        return analytics
    }

    override fun dataStore(storage: String): DataStore {
        return SharedPreferencesStore(appContext, storage)
    }

    override fun scheduler(): Scheduler {
        return androidScheduler
    }

    override fun notificationPublisher(): NotificationPublisher {
        return notificationPublisher
    }
}