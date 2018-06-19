package org.kinecosystem.kinit.mock

import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.network.ServicesProvider
import org.kinecosystem.kinit.network.TaskService
import org.kinecosystem.kinit.network.Wallet
import org.kinecosystem.kinit.notification.NotificationPublisher
import org.kinecosystem.kinit.repository.*
import org.mockito.Mockito

class MockComponentsProvider : DataStoreProvider {

    var userRepository: UserRepository = Mockito.mock(UserRepository::class.java)
    var questionnaireRepository = Mockito.mock(QuestionnaireRepository::class.java)
    var offersRepository = Mockito.mock(OffersRepository::class.java)
    var servicesProvider: ServicesProvider = Mockito.mock(ServicesProvider::class.java)
    var analytics: org.kinecosystem.kinit.analytics.Analytics = Mockito.mock(Analytics::class.java)
    var scheduler = MockScheduler()
    var wallet: Wallet = Mockito.mock(Wallet::class.java)
    val taskService:TaskService = Mockito.mock(TaskService::class.java)
    var notificationPublisher: NotificationPublisher = Mockito.mock(NotificationPublisher::class.java)


    override fun dataStore(storage: String): DataStore {
        return MockDataStore()
    }

}
