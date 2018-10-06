package org.kinecosystem.kinit.daggerCore

import android.content.Context
import org.kinecosystem.kinit.dagger.ContextModule
import org.kinecosystem.kinit.dagger.DataStoreProviderModule
import org.kinecosystem.kinit.mocks.MockDataStoreProviderProvider
import org.kinecosystem.kinit.repository.DataStoreProvider
import org.mockito.Mockito.mock

class TestCoreComponentProvider {
    var dataStoreProvider: DataStoreProvider = MockDataStoreProviderProvider()
    var coreComponent: TestCoreComponent

    init {
        coreComponent = DaggerTestCoreComponent.builder().contextModule(
                ContextModule(mock(Context::class.java)))
                .dataStoreProviderModule(DataStoreProviderModule(dataStoreProvider))
                .schedulerModule(TestSchedulerModule())
                .navigatorModule(TestNavigatorModule())
                .userRepositoryModule(TestUserRepositoryModule())
                .tasksRepositoryModule(TestTasksRepositoryModule())
                .offersRepositoryModule(TestOffersRepositoryModule())
                .analyticsModule(TestAnalyticsModule())
                .notificationModule(TestNotificationModule())
                .servicesProviderModule(TestServicesProviderModule())
                .build()
    }
}