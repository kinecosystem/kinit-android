package org.kinecosystem.kinit.dagger;

import org.kinecosystem.kinit.repository.DataStoreProvider;
import org.kinecosystem.kinit.repository.UserRepository;

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
