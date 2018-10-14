package org.kinecosystem.kinit.repository

import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.model.earn.*
import org.kinecosystem.kinit.util.ImageUtils
import javax.inject.Inject

private const val CATEGORIES_STORAGE = "kin.app.categories"
private const val GENERAL_STORAGE = "kin.app.general"

private const val CATEGORIES_KEY = "categories"
private const val HEADER_MESSAGE_KEY = "headerMessage"
private const val SHOW_CAPTCHA = "kin.app.SHOW_CAPTCHA"


class CategoriesRepository {
    @Inject
    lateinit var dataStoreProvider: DataStoreProvider

    var categories: ObservableField<List<Category>> = ObservableField(ArrayList())
    private var headerMessage: HeaderMessage? = null
    var headerTitle = ObservableField<String>()
    var headerSubtitle = ObservableField<String>()
    private var categoriesCache: DataStore
    private var generalCache: DataStore
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
        KinitApplication.coreComponent.inject(this)
        categoriesCache = dataStoreProvider.dataStore(CATEGORIES_STORAGE)
        generalCache = dataStoreProvider.dataStore(GENERAL_STORAGE)
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
        Log.d("###", "#### updateTasks updateTasks ")

        //TOOD check if need to replace!!!!
        for ((catId, tasksList) in updatedTasks) {
            if (tasks[catId] == null) {
                Log.d("###", "#### updateTasks null new repo $catId")
                tasks[catId] = TasksRepo(catId, tasksList.firstOrNull())
            } else {
                Log.d("###", "#### update tasksk   $catId  ${tasks[catId]}")
                tasks[catId]?.task = tasksList.firstOrNull()
                // val repos = tasks[catId]
            }
        }
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

    fun isTaskComplete(): Boolean {
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
            if (categoryId.equals(category.id)) {
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

    fun updateTaskState(taskId: String, newState: Int) {
        if (taskId == currentTaskRepo?.taskInProgress?.id) {
            currentTaskRepo?.taskState = newState
        }
    }

    fun onCategorySelected(category: Category? = null) {
        currentCategoryId = category?.id
        currentTaskRepo = getTaskRepo()
        category?.availableTasksCount?.let { currentAvailableTasks.set(it) }
        Log.d("#####", "#### cat id $currentCategoryId  task repo $currentTaskRepo task in progress $currentTaskInProgress")
    }

    fun getNumOfAnsweredQuestions(): Int {
        currentTaskRepo?.let {
            return it.getNumOfAnsweredQuestions()
        }
        return 0
    }

    fun getChosenAnswers(): ArrayList<ChosenAnswers> {
        return currentTaskRepo?.let {
            it.getChosenAnswers()
        } ?: ArrayList()
    }

    fun onTaskStarted() {
        currentTaskRepo?.onTaskStarted()
        currentTaskInProgress = if (currentTaskRepo != null) {
            currentTaskRepo?.taskInProgress
        } else {
            null
        }
        Log.d("#####", "#### cat id $currentCategoryId  task repo $currentTaskRepo task in progress $currentTaskInProgress")
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

