package org.kinecosystem.tippic.daggerCore

import org.kinecosystem.tippic.analytics.Analytics
import org.kinecosystem.tippic.dagger.AnalyticsModule
import org.mockito.Mockito.mock

class TestAnalyticsModule : AnalyticsModule() {
    override fun analytics(): Analytics {
        return mock(Analytics::class.java)
    }
}
