package org.kinecosystem.kinit.daggerTestCore

import org.kinecosystem.kinit.dagger.TasksRepositoryModule
import org.kinecosystem.kinit.repository.DataStoreProvider
import org.kinecosystem.kinit.repository.TasksRepository
import org.mockito.Mockito.mock

class TestTasksRepositoryModule : TasksRepositoryModule() {
    override fun tasksRepository(dataStoreProvider: DataStoreProvider?): TasksRepository {
        return mock(TasksRepository::class.java)
    }

}
