package org.kinecosystem.kinit.daggerTestCore

import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.dagger.AnalyticsModule
import org.mockito.Mockito.mock

class TestAnalyticsModule : AnalyticsModule() {
    override fun analytics(): Analytics {
        return mock(Analytics::class.java)
    }
}
