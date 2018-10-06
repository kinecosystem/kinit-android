package org.kinecosystem.kinit.daggerCore

import org.kinecosystem.kinit.dagger.OffersRepositoryModule
import org.kinecosystem.kinit.repository.OffersRepository
import org.mockito.Mockito.mock

class TestOffersRepositoryModule : OffersRepositoryModule() {
    override fun offersRepository(): OffersRepository {
        return mock(OffersRepository::class.java)
    }
}
