package org.kinecosystem.kinit.dagger;

import org.kinecosystem.kinit.annotations.DebugOpenClass;
import org.kinecosystem.kinit.repository.DataStoreProvider;
import org.kinecosystem.kinit.repository.TasksRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@DebugOpenClass
@Module(includes = DataStoreProviderModule.class)
public class TasksRepositoryModule {

    @Provides
    @Singleton
    public TasksRepository tasksRepository(DataStoreProvider dataStoreProvider) {
        return new TasksRepository(dataStoreProvider, null);
    }
}
