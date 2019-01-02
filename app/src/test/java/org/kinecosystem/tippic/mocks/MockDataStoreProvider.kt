package org.kinecosystem.tippic.mocks

import org.kinecosystem.tippic.repository.DataStore
import org.kinecosystem.tippic.repository.DataStoreProvider

class MockDataStoreProvider : DataStoreProvider {
    override fun dataStore(storage: String): DataStore {
        return MockDataStore()
    }
}