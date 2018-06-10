package dagger;

import javax.inject.Singleton;
import org.kinecosystem.kinit.analytics.Analytics;
import org.kinecosystem.kinit.util.AndroidScheduler;
import org.kinecosystem.kinit.util.Scheduler;

@Module
public class AnalyticsModule {


    @Provides
    @Singleton
    public Analytics analytics() {
        return new Analytics();
    }

    @Provides
    @Singleton
    public Scheduler scheduler() {
        return new AndroidScheduler();
    }
}
