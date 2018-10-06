package org.kinecosystem.kinit.daggerTestCore

import org.kinecosystem.kinit.dagger.SchedulerModule
import org.kinecosystem.kinit.mock.MockScheduler
import org.kinecosystem.kinit.util.Scheduler

class TestSchedulerModule : SchedulerModule() {
    override fun scheduler(): Scheduler {
        return MockScheduler()
    }
}
