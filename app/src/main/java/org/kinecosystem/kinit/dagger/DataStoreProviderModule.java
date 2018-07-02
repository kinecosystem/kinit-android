package org.kinecosystem.kinit.dagger;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import org.kinecosystem.kinit.repository.DataStoreProvider;

@Module
public class DataStoreProviderModule {

    private DataStoreProvider dataStoreProvider;

    public DataStoreProviderModule(DataStoreProvider dataStoreProvider) {
        this.dataStoreProvider = dataStoreProvider;

    }

    @Provides
    @Singleton
    public DataStoreProvider dataStoreProvider() {
        return dataStoreProvider;
    }
}
