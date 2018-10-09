package org.kinecosystem.kinit.daggerCore

import org.kinecosystem.kinit.dagger.TasksRepositoryModule
import org.kinecosystem.kinit.repository.TasksRepository
import org.mockito.Mockito.mock

class TestTasksRepositoryModule : TasksRepositoryModule() {
    override fun tasksRepository(): TasksRepository {
        return mock(TasksRepository::class.java)
    }
}
