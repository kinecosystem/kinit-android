package org.kinecosystem.kinit.repository

import android.content.Context
import android.databinding.ObservableBoolean
import android.text.TextUtils
import com.google.gson.Gson
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.model.TaskState
import org.kinecosystem.kinit.model.earn.*
import org.kinecosystem.kinit.util.ImageUtils
import javax.inject.Inject

private const val QUESTIONNAIRE_ANSWERS_STORAGE = "kin.app.task.chosen.answers"
private const val TASK_STORAGE = "kin.app.task"
private const val TASK_KEY = "task"
private const val TASK_STATE_KEY = "task_state"
private const val SHOW_CAPTCHA_KEY = "show_captcha"

class TasksRepository {
    @Inject
    lateinit var dataStoreProvider: DataStoreProvider

    var task: Task?
    private var chosenAnswers: ArrayList<ChosenAnswers> = ArrayList()
    private val chosenAnswersCache: DataStore
    private var taskCache: DataStore
    private val taskStorageName: String
    var taskInProgress: Task? = null
        private set
    var isTaskStarted: ObservableBoolean
    var taskState: Int
        set(state) {
            taskCache.putInt(TASK_STATE_KEY, state)
        }
        get() {
            return this.taskCache.getInt(TASK_STATE_KEY, TaskState.IDLE)
        }
    var shoulShowCaptcha: Boolean
        set(shouldShow) {
            taskCache.putBoolean(SHOW_CAPTCHA_KEY, shouldShow)
        }
        get () {
            return taskCache.getBoolean(SHOW_CAPTCHA_KEY)
        }

    init {
        KinitApplication.coreComponent.inject(this)
        taskCache = dataStoreProvider.dataStore(TASK_STORAGE)
        task = getCachedTask()
        taskStorageName = QUESTIONNAIRE_ANSWERS_STORAGE
        chosenAnswersCache = dataStoreProvider.dataStore(taskStorageName)
        isTaskStarted = ObservableBoolean(taskState != TaskState.IDLE)
    }

    fun setTask(task: String) {
        this.task = Gson().fromJson(task, Task::class.java)
    }

    fun resetTaskState() {
        taskState = if (getChosenAnswers().size > 0)
            TaskState.TASK_START_ANSWERED
        else
            TaskState.IDLE
    }

    private fun getCachedTask(): Task? {
        val cachedTask = taskCache.getString(TASK_KEY, "")
        return Gson().fromJson(cachedTask, Task::class.java)
    }

    fun setChosenAnswers(questionId: String, answersIds: List<String>) {
        chosenAnswers.add(ChosenAnswers(questionId, answersIds))
        chosenAnswersCache.putStringList(questionId, answersIds)
        isTaskStarted.set(true)
    }

    fun getChosenAnswersByQuestionId(questionId: String): List<String> = chosenAnswersCache.getStringList(questionId,
            listOf())

    fun getNumOfAnsweredQuestions(): Int {
        return getChosenAnswers().size
    }

    fun isTaskComplete(): Boolean {
        return task?.questions?.size == getNumOfAnsweredQuestions()
    }

    fun getChosenAnswers(): ArrayList<ChosenAnswers> {
        if (chosenAnswers.isEmpty()) {
            val answersMap = chosenAnswersCache.getAll()
            for (answers in answersMap) {
                if (answers.value is String) {
                    chosenAnswers.add(ChosenAnswers(answers.key, listOf(answers.value as String)))
                } else if (answers.value is HashSet<*>) {
                    chosenAnswers.add(ChosenAnswers(answers.key, (answers.value as HashSet<String>).toList()))
                }
            }
        }
        return chosenAnswers
    }

    fun isTaskAvailable(): Boolean {
        task?.startDateInMillis()?.let {
            return System.currentTimeMillis() >= it
        }
        return false
    }

    fun clearChosenAnswers() {
        chosenAnswers.clear()
        chosenAnswersCache.clearAll()
        taskState = TaskState.IDLE
        isTaskStarted.set(false)
    }

    fun replaceTask(task: Task?, applicationContext: Context) {
        this.task = task
        if (task != null) {
            taskCache.putString(TASK_KEY, Gson().toJson(task))
            if (task.hasPostActions()) {
                val url = task.postTaskActions?.first()?.iconUrl

                if (!TextUtils.isEmpty(url)) {
                    ImageUtils.fetchImage(applicationContext, url)
                }
            }
            for (question: Question in task.questions.orEmpty()) {
                if (question.hasImages()) {
                    ImageUtils.fetchImages(applicationContext, question.getImagesUrls())
                }
            }
        } else {
            taskCache.clear(TASK_KEY)
        }
        clearChosenAnswers()
    }

    fun onTaskStarted() {
        taskState = TaskState.IN_PROGRESS
        taskInProgress = task?.copy()
    }
}

