package org.kinecosystem.tippic.daggerCore

import dagger.Component
import org.kinecosystem.tippic.dagger.*
import javax.inject.Singleton

@Singleton
@Component(
        modules = [(ContextModule::class), (UserRepositoryModule::class), (AnalyticsModule::class), (NotificationModule::class), (DataStoreModule::class), (ServicesModule::class), (SchedulerModule::class)])
interface TestCoreComponent : CoreComponent