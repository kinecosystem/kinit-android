package org.kinecosystem.tippic.dagger;

import android.content.Context;

import org.kinecosystem.tippic.analytics.Analytics;
import org.kinecosystem.tippic.notification.NotificationPublisher;

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
