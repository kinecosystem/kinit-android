package org.kinecosystem.kinit.dagger

import dagger.Module
import dagger.Provides
import org.kinecosystem.kinit.util.AndroidScheduler
import org.kinecosystem.kinit.util.Scheduler
import javax.inject.Singleton


@DebugOpenClass
@Module
class SchedulerModule {

    @Provides
    @Singleton
    fun scheduler(): Scheduler = AndroidScheduler()
}
