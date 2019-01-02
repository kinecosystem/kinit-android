package org.kinecosystem.tippic.daggerCore

import org.kinecosystem.tippic.dagger.OffersRepositoryModule
import org.kinecosystem.tippic.repository.OffersRepository
import org.mockito.Mockito.mock

class TestOffersRepositoryModule : OffersRepositoryModule() {
    override fun offersRepository(): OffersRepository {
        return mock(OffersRepository::class.java)
    }
}
