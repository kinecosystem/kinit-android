package org.kinecosystem.tippic.daggerCore

import dagger.Component
import org.kinecosystem.tippic.dagger.*
import org.kinecosystem.tippic.repository.TaskTest
import org.kinecosystem.tippic.viewmodel.CategoriesViewModelTest
import org.kinecosystem.tippic.viewmodel.CategoryTaskViewModelTest
import org.kinecosystem.tippic.viewmodel.RestoreViewModelTest
import javax.inject.Singleton

@Singleton
@Component(
        modules = [(ContextModule::class), (UserRepositoryModule::class), (OffersRepositoryModule::class), (EcoAppsRepositoryModule::class), (AnalyticsModule::class), (NotificationModule::class), (DataStoreModule::class), (ServicesModule::class), (SchedulerModule::class)])
interface TestCoreComponent : CoreComponent {
    fun inject(restoreViewModelTest: RestoreViewModelTest)
    fun inject(earnViewModelTest: CategoryTaskViewModelTest)
    fun inject(categoriesViewModelTest: CategoriesViewModelTest)
    fun inject(taskTest: TaskTest)
}