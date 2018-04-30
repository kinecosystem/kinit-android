package org.kinecosystem.kinit.mock

import org.kinecosystem.kinit.repository.DataStore

class MockDataStore : DataStore {
    private val store: HashMap<String, Object> = HashMap()

    override fun getBoolean(key: String): Boolean {
        return store.get(key) as Boolean
    }

    override fun getBoolean(key: String, value: Boolean): Boolean {
        return store.get(key) as Boolean
    }

    override fun putBoolean(key: String, value: Boolean) {
        store.put(key, value as Object)
    }

    override fun getInt(key: String, defaultValue: Int?): Int {
        val res = store.get(key)
        return if (res != null)
            res as Int
        else defaultValue ?: 0
    }

    override fun putInt(key: String, value: Int) {
        store.put(key, value as Object)
    }

    override fun getLong(key: String, defaultValue: Long?): Long {
        val res = store.get(key)
        return if (res != null)
            res as Long
        else defaultValue ?: 0
    }

    override fun putLong(key: String, value: Long) {
        store.put(key, value as Object)
    }

    override fun getString(key: String, defaultValue: String?): String {
        val res = store.get(key)
        return if (res != null)
            res as String
        else defaultValue?:""
    }

    override fun getStringList(key: String, defaultValue: List<String>?): List<String>? {
        val res = store.get(key)
        return if (res != null)
            res as List<String>
        else defaultValue
    }

    override fun putString(key: String, value: String) {
        store.put(key, value as Object)
    }

    override fun putStringList(key: String, value: List<String>?) {
        store.put(key, value as Object)
    }

    override fun clear(key: String) {
        store.remove(key)
    }

    override fun clearAll() {
        store.clear()
    }

    override fun getAll(): Map<String, String> {
        return store as Map<String, String>
    }
}