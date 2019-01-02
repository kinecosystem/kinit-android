package org.kinecosystem.tippic.daggerCore

import android.content.Context
import org.kinecosystem.tippic.analytics.Analytics
import org.kinecosystem.tippic.dagger.NotificationModule
import org.kinecosystem.tippic.notification.NotificationPublisher
import org.mockito.Mockito.mock

class TestNotificationModule : NotificationModule() {
    override fun notificationPublisher(context: Context?, analytics: Analytics?): NotificationPublisher {
        return mock(NotificationPublisher::class.java)
    }
}
