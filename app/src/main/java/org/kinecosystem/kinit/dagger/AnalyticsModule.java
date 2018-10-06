package org.kinecosystem.kinit.dagger;

import org.kinecosystem.kinit.analytics.Analytics;
import org.kinecosystem.kinit.annotations.DebugOpenClass;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@DebugOpenClass
@Module
public class AnalyticsModule {


    @Provides
    @Singleton
    public Analytics analytics() {
        return new Analytics();
    }
}
