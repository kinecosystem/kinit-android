package dagger;

import javax.inject.Singleton;
import org.kinecosystem.kinit.util.AndroidScheduler;
import org.kinecosystem.kinit.util.Scheduler;

@Module
public class SchedulerModule {

    @Provides
    @Singleton
    public Scheduler scheduler() {
        return new AndroidScheduler();
    }
}
