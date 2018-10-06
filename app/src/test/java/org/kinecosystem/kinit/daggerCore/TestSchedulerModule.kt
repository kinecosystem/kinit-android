package org.kinecosystem.kinit.daggerCore

import org.kinecosystem.kinit.dagger.SchedulerModule
import org.kinecosystem.kinit.mocks.MockScheduler
import org.kinecosystem.kinit.util.Scheduler

class TestSchedulerModule : SchedulerModule() {
    override fun scheduler(): Scheduler {
        return MockScheduler()
    }
}
