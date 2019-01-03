package org.kinecosystem.tippic.dagger;

import org.kinecosystem.tippic.repository.PictureRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PictureRepositoryModule {

    @Provides
    @Singleton
    public PictureRepository pictureRepository() {
        return new PictureRepository();
    }
}
