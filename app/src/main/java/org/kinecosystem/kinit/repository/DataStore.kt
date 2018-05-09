package org.kinecosystem.kinit.repository

interface DataStore {
    fun getBoolean(key: String): Boolean
    fun getBoolean(key: String, value: Boolean): Boolean
    fun putBoolean(key: String, value: Boolean)
    fun getInt(key: String, defaultValue: Int?): Int
    fun putInt(key: String, value: Int)
    fun getLong(key: String, defaultValue: Long?): Long
    fun putLong(key: String, value: Long)
    fun getString(key: String, defaultValue: String?): String
    fun getStringList(key: String, defaultValue: List<String>): List<String>
    fun putString(key: String, value: String)
    fun putStringList(key: String, value: List<String>?)
    fun clear(key: String)
    fun clearAll()
    fun getAll(): Map<String, *>
}
