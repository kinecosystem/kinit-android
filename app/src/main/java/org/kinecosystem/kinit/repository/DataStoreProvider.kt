package org.kinecosystem.kinit.repository

interface DataStoreProvider {

    fun dataStore(storage: String): DataStore
}
