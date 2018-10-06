package org.kinecosystem.kinit.dagger;

import android.content.Context;

import org.kinecosystem.kinit.analytics.Analytics;
import org.kinecosystem.kinit.annotations.DebugOpenClass;
import org.kinecosystem.kinit.notification.NotificationPublisher;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@DebugOpenClass
@Module(includes = {ContextModule.class, AnalyticsModule.class})
public class NotificationModule {

    @Provides
    @Singleton
    public NotificationPublisher notificationPublisher(Context context, Analytics analytics) {
        return new NotificationPublisher(context, analytics);
    }

}
