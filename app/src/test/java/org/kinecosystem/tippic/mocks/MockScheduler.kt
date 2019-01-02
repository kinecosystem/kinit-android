package org.kinecosystem.tippic.mocks

import org.kinecosystem.tippic.util.Scheduler

class MockScheduler : Scheduler {

    private var runnable: Runnable? = null
    private var scheduledDelayMillis: Long = -1
    private var timeThatRequestWasOriginated: Long = -1
    private var currentTime: Long = -1

    fun setCurrentTime(time: Long) {
        currentTime = time
        if ((currentTime - timeThatRequestWasOriginated) >= scheduledDelayMillis) {
            runnable?.run()
        }
    }

    override fun post(runnable: Runnable?) {
        runnable?.run()
    }

    override fun scheduleOnMain(theRunnable: Runnable?, timeMilliSec: Long) {
        timeThatRequestWasOriginated = currentTimeMillis()
        runnable = theRunnable
        scheduledDelayMillis = timeMilliSec
    }

    override fun cancel(theRunnable: Runnable) {
    }

    override fun currentTimeMillis(): Long {
        return if (currentTime == -1L) {
            System.currentTimeMillis()
        } else {
            currentTime
        }
    }

    override fun executeOnBackground(runnable: Runnable?) {
        if (runnable != null) {
            runnable.run()
        }
    }
}