package org.kinecosystem.kinit.repository

import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.model.earn.Category
import org.kinecosystem.kinit.server.ServicesProvider
import org.kinecosystem.kinit.server.TaskService
import javax.inject.Inject

private const val CATEGORIES_STORAGE = "kin.app.categories"

class CategoriesTasksRepository {

    @Inject
    lateinit var dataStoreProvider: DataStoreProvider

    @Inject
    lateinit var taskService: TaskService

    @Inject
    lateinit var servicesProvider: ServicesProvider


    var tasks: MutableMap<String, TasksRepository> = mutableMapOf()
    private val taskCache: DataStore


    init {
        KinitApplication.coreComponent.inject(this)
        taskCache = dataStoreProvider.dataStore(CATEGORIES_STORAGE)
        loadCategories()
    }

    fun getCategories(): List<Category>? {
        //look in cache retrun from cache
        loadCategories()
        //get from server update cache
        return null

    }

    private fun loadCategories() {
        servicesProvider.taskService.retrieveCategories()
        taskService.retrieveCategories()
    }


}

