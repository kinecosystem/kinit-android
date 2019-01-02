package org.kinecosystem.tippic.dagger;

import org.kinecosystem.tippic.repository.DataStoreProvider;
import org.kinecosystem.tippic.repository.UserRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@DebugOpenClass
@Module(includes = DataStoreModule.class)
public class UserRepositoryModule {

    @Provides
    @Singleton
    public UserRepository userRepository(DataStoreProvider dataStoreProvider) {
        return new UserRepository(dataStoreProvider);
    }
}
