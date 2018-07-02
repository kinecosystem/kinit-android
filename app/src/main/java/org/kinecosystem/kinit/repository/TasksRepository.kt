package org.kinecosystem.kinit.repository

import android.content.Context
import android.databinding.ObservableBoolean
import android.util.Log
import com.google.gson.Gson
import org.kinecosystem.kinit.model.TaskState
import org.kinecosystem.kinit.model.earn.*
import org.kinecosystem.kinit.util.ImageUtils

private const val QUESTIONNAIRE_ANSWERS_STORAGE = "kin.app.task.chosen.answers"
private const val QUESTIONNAIRE_STORAGE = "kin.app.task"
private const val QUESTIONNAIRE_KEY = "task"
private const val TASK_STATE_KEY = "task_state"

class TasksRepository(dataStoreProvider: DataStoreProvider, defaultTask: String? = null) {
    var task: Task?
        private set
    private var chosenAnswers: ArrayList<ChosenAnswers> = ArrayList()
    private val chosenAnswersCache: DataStore
    private val questionnaireCache: DataStore = dataStoreProvider.dataStore(QUESTIONNAIRE_STORAGE)
    private val questionnaireStorageName: String
    var isQuestionnaireStarted: ObservableBoolean
    var taskState: Int
        set(state) {
            Log.d("TasksRepository", "setting task state to $state")
            questionnaireCache.putInt(TASK_STATE_KEY, state)
        }
        get() {
            return this.questionnaireCache.getInt(TASK_STATE_KEY, TaskState.INITIAL)
        }


    init {
        task = getCachedTask(defaultTask)
        questionnaireStorageName = QUESTIONNAIRE_ANSWERS_STORAGE + task?.id
        chosenAnswersCache = dataStoreProvider.dataStore(questionnaireStorageName)
        isQuestionnaireStarted = ObservableBoolean(getChosenAnswers().size > 0)
    }

    fun resetTaskState() {
        taskState = TaskState.INITIAL
    }

    private fun getCachedTask(defaultTask: String?): Task? {
        val cachedQuestionnaire = questionnaireCache.getString(QUESTIONNAIRE_KEY, defaultTask)
        return Gson().fromJson(cachedQuestionnaire, Task::class.java)
    }

    fun setChosenAnswers(questionId: String, answersIds: List<String>) {
        chosenAnswers.add(ChosenAnswers(questionId, answersIds))
        chosenAnswersCache.putStringList(questionId, answersIds)
        isQuestionnaireStarted.set(true)
    }

    fun getChosenAnswersByQuestionId(questionId: String): List<String> = chosenAnswersCache.getStringList(questionId, listOf())

    fun getNumOfAnsweredQuestions(): Int {
        return getChosenAnswers().size
    }

    fun isQuestionnaireComplete(): Boolean {
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

    fun isQuestionnaireAvaliable(): Boolean {
        if (task == null) return false
        val taskDate: Long = task?.startDateInMillis() ?: System.currentTimeMillis()
        return System.currentTimeMillis() >= taskDate
    }

    fun clearChosenAnswers() {
        chosenAnswers.clear()
        chosenAnswersCache.clearAll()
        isQuestionnaireStarted.set(false)
    }

    fun replaceQuestionnaire(task: Task?, applicationContext: Context) {
        this.task = task
        if (task != null) {
            questionnaireCache.putString(QUESTIONNAIRE_KEY, Gson().toJson(task))
            for (question: Question in task.questions.orEmpty()) {
                if (Question.QuestionType.TEXT_IMAGE.type == question.type) {
                    for (answer: Answer in question.answers.orEmpty())
                        ImageUtils.fetchImages(applicationContext, answer.imageUrl)
                }
            }
        } else {
            questionnaireCache.clear(QUESTIONNAIRE_KEY)
        }
        clearChosenAnswers()
    }
}

