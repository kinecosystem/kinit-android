package org.kinecosystem.tippic.repository

import android.databinding.ObservableBoolean
import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.model.TaskState
import org.kinecosystem.tippic.model.earn.ChosenAnswers
import org.kinecosystem.tippic.model.earn.Task
import javax.inject.Inject

private const val QUESTIONNAIRE_ANSWERS_STORAGE = "kin.app.task.chosen.answers"
private const val TASK_STORAGE = "kin.app.task"
private const val TASK_STATE_KEY = "task_state"

class TasksRepo(categoryId: String, var task: Task?) {
    @Inject
    lateinit var dataStoreProvider: DataStoreProvider
    private var chosenAnswers: ArrayList<ChosenAnswers> = ArrayList()
    private val chosenAnswersCache: DataStore
    private var taskCache: DataStore
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

    init {
        TippicApplication.coreComponent.inject(this)
        taskCache = dataStoreProvider.dataStore(TASK_STORAGE + categoryId)
        chosenAnswersCache = dataStoreProvider.dataStore(QUESTIONNAIRE_ANSWERS_STORAGE + categoryId)
        isTaskStarted = ObservableBoolean(taskState != TaskState.IDLE)
    }

    fun resetTaskState() {
        taskState = if (getChosenAnswers().size > 0)
            TaskState.TASK_START_ANSWERED
        else
            TaskState.IDLE
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

    fun clearChosenAnswers() {
        chosenAnswers.clear()
        chosenAnswersCache.clearAll()
        taskState = TaskState.IDLE
        isTaskStarted.set(false)
    }

    fun onTaskStarted() {
        taskState = TaskState.IN_PROGRESS
        taskInProgress = task?.copy()
    }

    fun updateTestTask(startDateInSeconds: Long) {
        task = task?.copy(startDateInSeconds = startDateInSeconds)
        if (taskInProgress?.id == task?.id) {
            taskInProgress = task?.copy()
        }
    }
}

