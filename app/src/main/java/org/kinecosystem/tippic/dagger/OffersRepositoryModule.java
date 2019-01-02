package org.kinecosystem.tippic.dagger;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import org.kinecosystem.tippic.repository.OffersRepository;

@Module
public class OffersRepositoryModule {

    @Provides
    @Singleton
    public OffersRepository offersRepository() {
        return new OffersRepository();
    }
}
