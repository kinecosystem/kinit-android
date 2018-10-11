package org.kinecosystem.kinit.dagger;

import org.kinecosystem.kinit.repository.TasksRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@DebugOpenClass
@Module
public class TasksRepositoryModule {

    @Provides
    @Singleton
    public TasksRepository tasksRepository() {
        return new TasksRepository();
    }
}
