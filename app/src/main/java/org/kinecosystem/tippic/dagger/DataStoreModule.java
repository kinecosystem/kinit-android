package org.kinecosystem.tippic.dagger;

import org.kinecosystem.tippic.repository.DataStoreProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataStoreModule {

    private DataStoreProvider dataStoreProvider;

    public DataStoreModule(DataStoreProvider dataStoreProvider) {
        this.dataStoreProvider = dataStoreProvider;

    }

    @Provides
    @Singleton
    public DataStoreProvider dataStoreProvider() {
        return dataStoreProvider;
    }
}
