package dagger;

import android.content.Context;
import javax.inject.Singleton;
import org.kinecosystem.kinit.analytics.Analytics;
import org.kinecosystem.kinit.notification.NotificationPublisher;

@Module(includes = {ContextModule.class, AnalyticsModule.class})
public class NotificationModule {

    @Provides
    @Singleton
    public NotificationPublisher notificationPublisher(Context context, Analytics analytics) {
        return new NotificationPublisher(context, analytics);
    }

}
