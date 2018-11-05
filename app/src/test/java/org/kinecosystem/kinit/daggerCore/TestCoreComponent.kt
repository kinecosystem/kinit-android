package org.kinecosystem.kinit.daggerCore

import dagger.Component
import org.kinecosystem.kinit.dagger.*
import org.kinecosystem.kinit.repository.TaskTest
import org.kinecosystem.kinit.viewmodel.CategoriesViewModelTest
import org.kinecosystem.kinit.viewmodel.EarnViewModelTest
import org.kinecosystem.kinit.viewmodel.RestoreViewModelTest
import javax.inject.Singleton

@Singleton
@Component(
        modules = [(ContextModule::class), (UserRepositoryModule::class), (OffersRepositoryModule::class), (AnalyticsModule::class), (NotificationModule::class), (DataStoreModule::class), (ServicesModule::class), (SchedulerModule::class)])
interface TestCoreComponent : CoreComponent {
    fun inject(restoreViewModelTest: RestoreViewModelTest)
    fun inject(earnViewModelTest: EarnViewModelTest)
    fun inject(categoriesViewModelTest: CategoriesViewModelTest)
    fun inject(taskTest: TaskTest)
}