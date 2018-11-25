package org.kinecosystem.kinit.dagger;

import org.kinecosystem.kinit.repository.EcoApplicationsRepository;
import org.kinecosystem.kinit.repository.OffersRepository;

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
