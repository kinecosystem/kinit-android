package org.kinecosystem.kinit.daggerTestCore

import android.content.Context
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.dagger.NotificationModule
import org.kinecosystem.kinit.notification.NotificationPublisher
import org.mockito.Mockito.mock

class TestNotificationModule : NotificationModule() {
    override fun notificationPublisher(context: Context?, analytics: Analytics?): NotificationPublisher {
        return mock(NotificationPublisher::class.java)
    }
}
