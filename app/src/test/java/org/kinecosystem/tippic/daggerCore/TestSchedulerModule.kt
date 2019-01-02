package org.kinecosystem.tippic.daggerCore

import org.kinecosystem.tippic.dagger.SchedulerModule
import org.kinecosystem.tippic.mocks.MockScheduler
import org.kinecosystem.tippic.util.Scheduler

class TestSchedulerModule : SchedulerModule() {
    override fun scheduler(): Scheduler {
        return MockScheduler()
    }
}
