package org.kinecosystem.tippic.dagger;

import org.kinecosystem.tippic.repository.EcoApplicationsRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class EcoAppsRepositoryModule {

    @Provides
    @Singleton
    public EcoApplicationsRepository EcoApplicationsRepository() {
        return new EcoApplicationsRepository();
    }
}
