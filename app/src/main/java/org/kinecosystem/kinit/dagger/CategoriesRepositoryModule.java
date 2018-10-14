package org.kinecosystem.kinit.dagger;

import org.kinecosystem.kinit.repository.CategoriesRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@DebugOpenClass
@Module
public class CategoriesRepositoryModule {

    @Provides
    @Singleton
    public CategoriesRepository categoryRepository() {
        return new CategoriesRepository();
    }
}
