package org.kinecosystem.kinit.mock

import org.kinecosystem.kinit.repository.DataStore
import org.kinecosystem.kinit.repository.DataStoreProvider

class MockDataStoreProviderProvider : DataStoreProvider {
    override fun dataStore(storage: String): DataStore {
        return MockDataStore()
    }
}