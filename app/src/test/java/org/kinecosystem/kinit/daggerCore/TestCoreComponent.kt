package org.kinecosystem.kinit.daggerCore

import dagger.Component
import org.kinecosystem.kinit.dagger.*
import org.kinecosystem.kinit.viewmodel.EarnViewModelTest
import org.kinecosystem.kinit.viewmodel.RestoreViewModelTest
import javax.inject.Singleton

@Singleton
@Component(
        modules = [(ContextModule::class), (UserRepositoryModule::class), (TasksRepositoryModule::class), (OffersRepositoryModule::class), (AnalyticsModule::class), (NotificationModule::class), (DataStoreModule::class), (ServicesModule::class), (SchedulerModule::class), (NavigatorModule::class)])
interface TestCoreComponent : CoreComponent {
    fun inject(restoreViewModelTest: RestoreViewModelTest)
    fun inject(earnViewModelTest: EarnViewModelTest)
}