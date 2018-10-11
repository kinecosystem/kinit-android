package org.kinecosystem.kinit.daggerCore

import android.content.Context
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.dagger.ContextModule
import org.kinecosystem.kinit.dagger.DataStoreModule
import org.kinecosystem.kinit.mocks.MockDataStoreProvider
import org.mockito.Mockito.mock

class TestUtils {

    companion object {
        fun setupCoreComponent(): TestCoreComponent {
            val comp = DaggerTestCoreComponent.builder().contextModule(
                    ContextModule(mock(Context::class.java)))
                    .dataStoreModule(DataStoreModule(MockDataStoreProvider()))
                    .schedulerModule(TestSchedulerModule())
                    .navigatorModule(TestNavigatorModule())
                    .userRepositoryModule(TestUserRepositoryModule())
                    .tasksRepositoryModule(TestTasksRepositoryModule())
                    .offersRepositoryModule(TestOffersRepositoryModule())
                    .analyticsModule(TestAnalyticsModule())
                    .notificationModule(TestNotificationModule())
                    .servicesModule(TestServicesModule())
                    .build()

            KinitApplication.coreComponent = comp
            return comp
        }
    }
}