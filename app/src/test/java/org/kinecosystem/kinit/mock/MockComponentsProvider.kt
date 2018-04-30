package org.kinecosystem.kinit.mock

import org.kinecosystem.kinit.CoreComponentsProvider
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.network.ServicesProvider
import org.kinecosystem.kinit.network.Wallet
import org.kinecosystem.kinit.notification.NotificationPublisher
import org.kinecosystem.kinit.repository.DataStore
import org.kinecosystem.kinit.repository.OffersRepository
import org.kinecosystem.kinit.repository.QuestionnaireRepository
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.util.Scheduler
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class MockComponentsProvider : CoreComponentsProvider {
    var userRepository: UserRepository = Mockito.mock(UserRepository::class.java)
    var questionnaireRepository = Mockito.mock(QuestionnaireRepository::class.java)
    var offersRepository = Mockito.mock(OffersRepository::class.java)
    var servicesProvider: ServicesProvider = Mockito.mock(ServicesProvider::class.java)
    var analytics: org.kinecosystem.kinit.analytics.Analytics = Mockito.mock(Analytics::class.java)
    var scheduler = MockScheduler()
    var wallet: Wallet = Mockito.mock(Wallet::class.java)
    var notificationPublisher: NotificationPublisher = Mockito.mock(NotificationPublisher::class.java)

    init {
        `when`(servicesProvider.walletService).thenReturn(wallet)
    }

    override fun userRepo(): UserRepository {
        return userRepository
    }

    override fun questionnaireRepo(): QuestionnaireRepository {
        return questionnaireRepository
    }

    override fun offersRepo(): OffersRepository {
        return offersRepository
    }

    override fun services(): ServicesProvider {
        return servicesProvider
    }

    override fun analytics(): Analytics {
        return analytics
    }

    override fun dataStore(storage: String): DataStore {
        return MockDataStore()
    }

    override fun scheduler(): Scheduler {
        return scheduler
    }

    override fun notificationPublisher(): NotificationPublisher {
        return notificationPublisher
    }
}
