package org.kinecosystem.kinit.daggerTestCore

import android.content.Context
import org.kinecosystem.kinit.dagger.ContextModule
import org.kinecosystem.kinit.dagger.DataStoreProviderModule
import org.kinecosystem.kinit.mock.MockDataStoreProviderProvider
import org.mockito.Mockito.mock

class TestCoreComponentProvider private constructor() {
    private object Holder {
        val INSTANCE = TestCoreComponentProvider()
    }

    companion object {
        val instance: TestCoreComponentProvider by lazy { Holder.INSTANCE }
    }

    val dataStoreProvider = MockDataStoreProviderProvider()
    val coreComponent: TestCoreComponent = DaggerTestCoreComponent.builder().contextModule(
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