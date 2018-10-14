package org.kinecosystem.kinit.repository

import android.databinding.ObservableField
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.model.earn.Category
import org.kinecosystem.kinit.model.earn.HeaderMessage
import org.kinecosystem.kinit.model.earn.Task
import java.util.*
import javax.inject.Inject

private const val CATEGORIES_STORAGE = "kin.app.categories"
private const val CATEGORIES_KEY = "categories"
private const val HEADER_MESSAGE_KEY = "headerMessage"


class CategoriesRepository {
    @Inject
    lateinit var dataStoreProvider: DataStoreProvider

    var categories: ObservableField<List<Category>> = ObservableField(ArrayList())
    private var headerMessage: HeaderMessage? = null
    var headerTitle = ObservableField<String>()
    var headerSubtitle = ObservableField<String>()
    private var categoriesCache: DataStore
    private var tasks: MutableMap<String, List<TasksRepo>> = mutableMapOf()


    init {
        KinitApplication.coreComponent.inject(this)
        categoriesCache = dataStoreProvider.dataStore(CATEGORIES_STORAGE)
        updateFromCache()
    }

    fun updateCategories(newCategories: List<Category>?) {
        this.categories.set(newCategories)
        categoriesCache.getString(CATEGORIES_KEY, Gson().toJson(newCategories))
    }

    fun updateHeader(headerMsg: HeaderMessage?) {
        this.headerMessage = headerMsg
        headerMessage?.let {
            headerTitle.set(it.title)
            headerSubtitle.set(it.subtitle)
        }
        categoriesCache.getString(HEADER_MESSAGE_KEY, Gson().toJson(headerMsg))
    }

    private fun updateFromCache() {
        updateCategories(Gson().fromJson(categoriesCache.getString(CATEGORIES_KEY, ""), object : TypeToken<List<Category>>() {}.type))
        updateHeader(Gson().fromJson(categoriesCache.getString(HEADER_MESSAGE_KEY, ""), HeaderMessage::class.java))
    }

    fun updateTasks(updatedTasks: Map<String, List<Task>>) {
        for ((catId, tasksList) in updatedTasks) {
            tasks[catId] = tasksList.map { TasksRepo(catId, it.copy()) }
        }
    }

    fun getTask(category: Category): TasksRepo? {
        category.id?.let {
            return if (tasks.containsKey(it)) {
                tasks.getValue(it).firstOrNull()
            } else null
        }
        return null
    }

    fun getCategory(categoryId: String): Category? {
        for (category in categories.get()) {
            if (categoryId.equals(category.id)) {
                return category
            }
        }
        return null
    }

    fun updateAvialbleTask(taskCount: Int, categoryId: String) {
        for (category in categories.get()) {
            if (categoryId.equals(category.id)) {
                category.availableTasksCount = taskCount
            }
        }
    }

    fun updateTaskState(taskId: String, state: Int) {
        //TODO

    }
}

