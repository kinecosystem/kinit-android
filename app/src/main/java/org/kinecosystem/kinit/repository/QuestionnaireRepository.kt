package org.kinecosystem.kinit.repository

import android.content.Context
import android.databinding.ObservableBoolean
import android.util.Log
import com.google.gson.Gson
import org.kinecosystem.kinit.model.TaskState
import org.kinecosystem.kinit.model.earn.Answer
import org.kinecosystem.kinit.model.earn.ChosenAnswer
import org.kinecosystem.kinit.model.earn.Question
import org.kinecosystem.kinit.model.earn.Task
import org.kinecosystem.kinit.util.ImageUtils

private const val QUESTIONNAIRE_ANSWERS_STORAGE = "kin.app.task.chosen.answers"
private const val QUESTIONNAIRE_STORAGE = "kin.app.task"
private const val QUESTIONNAIRE_KEY = "task"
private const val TASK_STATE_KEY = "task_state"

class QuestionnaireRepository(dataStoreProvider: DataStoreProvider, defaultTask: String? = null) {
    var task: Task?
        private set
    private var chosenAnswers: ArrayList<ChosenAnswer> = ArrayList()
    private val chosenAnswersCache: DataStore
    private val questionnaireCache: DataStore = dataStoreProvider.dataStore(QUESTIONNAIRE_STORAGE)
    private val questionnaireStorageName: String
    var isQuestionnaireStarted: ObservableBoolean
    var taskState: Int
        set(state) {
            Log.d("QuestionnaireRepository", "setting task state to $state")
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

    fun setChosenAnswer(questionId: String, answerId: String) {
        chosenAnswers.add(ChosenAnswer(questionId, answerId))
        chosenAnswersCache.putString(questionId, answerId)
        isQuestionnaireStarted.set(true)
    }

    fun getChosenAnswer(questionId: String): String = chosenAnswersCache.getString(questionId, "")

    fun getNumOfChosenAnswers(): Int = getChosenAnswers().size

    fun isQuestionnaireComplete(): Boolean {
        return task?.questions?.size == getNumOfChosenAnswers()
    }

    fun getChosenAnswers(): ArrayList<ChosenAnswer> {
        if (chosenAnswers.isEmpty()) {
            val answersMap = chosenAnswersCache.getAll()
            for (answer in answersMap) {
                chosenAnswers.add(ChosenAnswer(answer.key, answer.value))
            }
        }
        return chosenAnswers
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
            for (question : Question in task.questions.orEmpty())
            {
                if (Question.QuestionType.TEXT_IMAGE.type == question.type) {
                    for (answer : Answer in question.answers.orEmpty())
                        ImageUtils.fetchImages(applicationContext, answer.imageUrl)
                }
            }
        } else {
            questionnaireCache.clear(QUESTIONNAIRE_KEY)
        }
        clearChosenAnswers()
    }
}

