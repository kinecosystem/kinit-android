package org.kinecosystem.kinit.dagger;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import org.kinecosystem.kinit.repository.DataStoreProvider;
import org.kinecosystem.kinit.repository.UserRepository;

@Module(includes = DataStoreProviderModule.class)
public class UserRepositoryModule {

    @Provides
    @Singleton
    public UserRepository userRepository(DataStoreProvider dataStoreProvider) {
        return new UserRepository(dataStoreProvider);
    }
}
