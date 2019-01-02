package org.kinecosystem.tippic.repository

interface DataStoreProvider {

    fun dataStore(storage: String): DataStore
}
