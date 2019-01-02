package org.kinecosystem.tippic.daggerCore

import android.content.Context
import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.dagger.ContextModule
import org.kinecosystem.tippic.dagger.DataStoreModule
import org.kinecosystem.tippic.mocks.MockDataStoreProvider
import org.mockito.Mockito.mock

class TestUtils {

    companion object {
        fun setupCoreComponent(): TestCoreComponent {
            val comp = DaggerTestCoreComponent.builder().contextModule(
                    ContextModule(mock(Context::class.java)))
                    .dataStoreModule(DataStoreModule(MockDataStoreProvider()))
                    .schedulerModule(TestSchedulerModule())
                    .userRepositoryModule(TestUserRepositoryModule())
                    .offersRepositoryModule(TestOffersRepositoryModule())
                    .analyticsModule(TestAnalyticsModule())
                    .notificationModule(TestNotificationModule())
                    .servicesModule(TestServicesModule())
                    .build()

            TippicApplication.coreComponent = comp
            return comp
        }
    }
}