package org.kinecosystem.kinit.viewmodel.earn

import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.support.v4.app.Fragment
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.TaskState
import org.kinecosystem.kinit.model.earn.isQuiz
import org.kinecosystem.kinit.model.earn.isTypeDualImage
import org.kinecosystem.kinit.model.earn.tagsString
import org.kinecosystem.kinit.repository.TasksRepository
import org.kinecosystem.kinit.view.earn.*
import javax.inject.Inject

const val NEXT_QUESTION_PAGE = 0
const val QUESTIONNAIRE_COMPLETE_PAGE = 1
const val REWARD_PAGE = 2
const val SUBMIT_ERROR_PAGE = 3
const val TRANSACTION_ERROR_PAGE = 4

open class QuestionnaireViewModel(taskId:String, restoreState: Boolean) :
        QuestionnaireActions {

    @Inject
    lateinit var taskRepository: TasksRepository
    @Inject
    lateinit var analytics: Analytics

    var questionnaireProgress: ObservableInt = ObservableInt()
    var nextFragment: ObservableField<Fragment> = ObservableField()
    var currentPageState: Int

    init {
        KinitApplication.coreComponent.inject(this)
        currentPageState =
                when {
                    restoreState -> getPageFromState()
                    !taskRepository.isTaskComplete() -> NEXT_QUESTION_PAGE
                    else -> QUESTIONNAIRE_COMPLETE_PAGE
                }
        moveToNextPage(currentPageState)
    }

    override fun next() {
        moveToNextPage(
                if (!taskRepository.isTaskComplete()) {
                    NEXT_QUESTION_PAGE
                } else QUESTIONNAIRE_COMPLETE_PAGE
        )
    }

    override fun transactionError() {
        moveToNextPage(TRANSACTION_ERROR_PAGE)
    }

    override fun submissionError() {
        moveToNextPage(SUBMIT_ERROR_PAGE)
    }

    override fun submissionAnimComplete() {
        moveToNextPage(REWARD_PAGE)
    }

    protected fun nextQuestionIndex(): Int {
        return taskRepository.getNumOfAnsweredQuestions()
    }

    protected fun questionIndex(): Int {
        return taskRepository.getNumOfAnsweredQuestions() - 1
    }

    protected fun moveToNextPage(pageState: Int) {
        currentPageState = pageState
        questionnaireProgress.set(
                ((taskRepository.getNumOfAnsweredQuestions().toDouble() / taskRepository.taskInProgress?.questions!!.size) * 100).toInt())
        nextFragment.set(getFragment())
    }

    open fun getFragment(): Fragment {
        return when (currentPageState) {
            NEXT_QUESTION_PAGE -> {
                when {
                    taskRepository.taskInProgress?.isQuiz()!! -> QuizFragment.newInstance(nextQuestionIndex())
                    taskRepository.taskInProgress?.questions?.get(nextQuestionIndex())?.isTypeDualImage()!! -> QuestionDualImageFragment.newInstance(nextQuestionIndex())
                    else -> QuestionFragment.newInstance(nextQuestionIndex())
                }
            }
            QUESTIONNAIRE_COMPLETE_PAGE -> QuestionnaireCompleteFragment.newInstance()
            REWARD_PAGE -> TaskRewardFragment.newInstance()
            TRANSACTION_ERROR_PAGE -> TaskErrorFragment.newInstance(
                    TaskErrorFragment.ERROR_TRANSACTION)
            else -> TaskErrorFragment.newInstance(TaskErrorFragment.ERROR_SUBMIT)
        }
    }

    fun onBackPressed() {
        val task = taskRepository.taskInProgress
        val event: Events.Event
        if (taskRepository.isTaskComplete()) {
            event = Events.Analytics.ClickCloseButtonOnRewardPage(task?.provider?.name,
                    task?.minToComplete,
                    task?.kinReward,
                    task?.tagsString(),
                    task?.id,
                    task?.title,
                    task?.type)
        } else {
            val question = task?.questions?.get(nextQuestionIndex())
            event = Events.Analytics.ClickCloseButtonOnQuestionPage(task?.provider?.name,
                    task?.minToComplete,
                    task?.kinReward,
                    question?.answers?.count(),
                    task?.questions?.count(),
                    question?.id,
                    nextQuestionIndex() + 1,
                    question?.type,
                    task?.tagsString(),
                    task?.id,
                    task?.title)
        }
        analytics.logEvent(event)
    }


    protected fun getPageFromState(): Int {
        return when {
            taskRepository.taskState == TaskState.SUBMITTED_SUCCESS_WAIT_FOR_REWARD -> REWARD_PAGE
            taskRepository.taskState == TaskState.TRANSACTION_COMPLETED -> REWARD_PAGE
            taskRepository.taskState == TaskState.TRANSACTION_ERROR -> TRANSACTION_ERROR_PAGE
            taskRepository.taskState == TaskState.SUBMIT_ERROR_RETRY -> QUESTIONNAIRE_COMPLETE_PAGE
            taskRepository.taskState == TaskState.SUBMIT_ERROR_NO_RETRY -> SUBMIT_ERROR_PAGE
            taskRepository.taskState == TaskState.SUBMITTED -> SUBMIT_ERROR_PAGE
            else -> {
                if (!taskRepository.isTaskComplete())
                    NEXT_QUESTION_PAGE
                else QUESTIONNAIRE_COMPLETE_PAGE
            }
        }
    }
}
