package org.kinecosystem.tippic.repository

import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.util.Log
import com.google.gson.Gson
import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.model.earn.Category
import org.kinecosystem.tippic.model.earn.HeaderMessage
import org.kinecosystem.tippic.model.earn.Task
import org.kinecosystem.tippic.server.api.CategoriesApi
import org.kinecosystem.tippic.server.api.TasksApi
import java.util.*
import javax.inject.Inject

private const val GENERAL_STORAGE = "kin.app.general"
private const val SHOW_CAPTCHA = "kin.app.SHOW_CAPTCHA"


class CategoriesRepository {
    @Inject
    lateinit var dataStoreProvider: DataStoreProvider

    var categories: ObservableField<List<Category>> = ObservableField(ArrayList())
    private var headerMessage: HeaderMessage? = null
    var headerTitle = ObservableField<String>()
    var headerSubtitle = ObservableField<String>()
    private var generalCache: DataStore
    var currentCategoryTitle: String = ""
        private set

    private var tasks: MutableMap<String, TasksRepo> = mutableMapOf()
    var currentTaskInProgress: Task? = null
        private set
    private var currentCategoryId: String? = null
    var currentTaskRepo: TasksRepo? = null
        private set
    var shouldShowCaptcha: Boolean
        set(value) {
            generalCache.putBoolean(SHOW_CAPTCHA, value)
        }
        get() {
            return generalCache.getBoolean(SHOW_CAPTCHA, false)
        }
    var currentAvailableTasks = ObservableInt()


    init {
        TippicApplication.coreComponent.inject(this)
        generalCache = dataStoreProvider.dataStore(GENERAL_STORAGE)
    }

    fun updateCategories(newCategories: List<Category>?) {
        newCategories?.let {
            this.categories.set(newCategories)
        }
    }

    fun updateTestData(categoriesData: String, tasksData: String) {
        val categoriesResponse = Gson().fromJson(categoriesData, CategoriesApi.CategoriesResponse::class.java)
        updateCategories(categoriesResponse.categories)
        updateHeader(categoriesResponse.headerMessage)

        val taskResponse = Gson().fromJson(tasksData, TasksApi.NextTasksResponse::class.java)
        updateTasks(taskResponse.tasksMap)
    }

    fun updateHeader(headerMsg: HeaderMessage?) {
        this.headerMessage = headerMsg
        headerMessage?.let {
            headerTitle.set(it.title)
            headerSubtitle.set(it.subtitle)
        }
    }

    fun updateTasks(updatedTasks: Map<String, List<Task>>) {
        for ((catId, tasksList) in updatedTasks) {
            if (tasks[catId] == null) {
                //Log.d("###", "#### updateTasks null new repo $catId")
                tasks[catId] = TasksRepo(catId, tasksList.firstOrNull())
            } else {
                //Log.d("###", "#### update tasksk   $catId  ${tasks[catId]}")
                tasks[catId]?.task = tasksList.firstOrNull()
                // val repos = tasks[catId]
            }
        }
    }

    fun hasAnyTask(): Boolean {
        for ((_, taskRepo) in tasks) {
            if (taskRepo.task != null) {
                return true
            }
        }
        return false
    }

    private fun getTaskRepo(): TasksRepo? {
        currentCategoryId?.let {
            return if (tasks.containsKey(it)) {
                tasks.getValue(it)
            } else null
        }
        return null
    }

    fun getCurrentTaskState() = currentTaskRepo?.taskState

    fun isCurrentTaskComplete(): Boolean {
        currentTaskRepo?.let {
            return it.isTaskComplete()
        }
        return false
    }

    fun getTask(categoryId: String): Task? {
        return if (tasks.containsKey(categoryId)) {
            return tasks.getValue(categoryId)?.task
        } else null
    }

    fun getCategory(categoryId: String): Category? {
        for (category in categories.get()) {
            if (categoryId == category.id) {
                return category
            }
        }
        return null
    }

    fun updateAvailableTasksCount(categoryId: String, taskCount: Int) {
        currentAvailableTasks.set(taskCount)
        for (category in categories.get()) {
            if (categoryId == category.id) {
                category.availableTasksCount = taskCount
            }
        }
    }

    fun updateCurrentTaskState(taskId: String, newState: Int) {
        if (taskId == currentTaskRepo?.taskInProgress?.id) {
            currentTaskRepo?.taskState = newState
        }
    }

    fun onCategorySelected(category: Category? = null) {
        currentCategoryId = category?.id
        currentCategoryTitle = category?.title ?: ""
        currentTaskRepo = getTaskRepo()
        category?.availableTasksCount?.let { currentAvailableTasks.set(it) }
    }

    fun getCurrentTaskAnsweredQuestionsCount(): Int {
        currentTaskRepo?.let {
            return it.getNumOfAnsweredQuestions()
        }
        return 0
    }

    fun onTaskStarted() {
        currentTaskRepo?.onTaskStarted()
        currentTaskInProgress = currentTaskRepo?.taskInProgress
        Log.d("#####", "#### onTaskStarted cat id $currentCategoryId  task repo $currentTaskRepo task in progress $currentTaskInProgress")
    }

    fun hasAnyTaskAvailable(): Boolean {
        if (categories.get() == null) return false
        for (category in categories.get()) {
            if (category.availableTasksCount != 0) {
                return true
            }
        }
        return false
    }

    fun updateNextTask(categoryId: String, task: Task?) {
        tasks[categoryId]?.task = task
    }
}

