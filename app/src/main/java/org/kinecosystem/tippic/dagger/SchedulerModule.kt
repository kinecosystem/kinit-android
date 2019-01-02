package org.kinecosystem.tippic.dagger

import dagger.Module
import dagger.Provides
import org.kinecosystem.tippic.util.AndroidScheduler
import org.kinecosystem.tippic.util.Scheduler
import javax.inject.Singleton


@DebugOpenClass
@Module
class SchedulerModule {

    @Provides
    @Singleton
    fun scheduler(): Scheduler = AndroidScheduler()
}
