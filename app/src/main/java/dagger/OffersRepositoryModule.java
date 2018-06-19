package dagger;

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
