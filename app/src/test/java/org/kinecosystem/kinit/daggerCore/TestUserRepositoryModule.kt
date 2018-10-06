package org.kinecosystem.kinit.daggerCore

import org.kinecosystem.kinit.dagger.UserRepositoryModule
import org.kinecosystem.kinit.repository.DataStoreProvider
import org.kinecosystem.kinit.repository.UserRepository
import org.mockito.Mockito.mock

class TestUserRepositoryModule : UserRepositoryModule() {
    override fun userRepository(dataStoreProvider: DataStoreProvider?): UserRepository {
        return mock(UserRepository::class.java)
    }
}
