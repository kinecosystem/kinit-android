package org.kinecosystem.tippic.daggerCore

import org.kinecosystem.tippic.dagger.UserRepositoryModule
import org.kinecosystem.tippic.repository.DataStoreProvider
import org.kinecosystem.tippic.repository.UserRepository
import org.mockito.Mockito.mock

class TestUserRepositoryModule : UserRepositoryModule() {
    override fun userRepository(dataStoreProvider: DataStoreProvider?): UserRepository {
        return mock(UserRepository::class.java)
    }
}
