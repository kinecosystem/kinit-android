package org.kinecosystem.kinit.dagger;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import org.kinecosystem.kinit.repository.OffersRepository;

@Module
public class OffersRepositoryModule {

    @Provides
    @Singleton
    public OffersRepository offersRepository() {
        return new OffersRepository();
    }
}
