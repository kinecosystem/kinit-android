package org.kinecosystem.kinit.viewmodel.earn

import android.databinding.ObservableBoolean
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
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.repository.CategoriesRepository
import org.kinecosystem.kinit.view.earn.*
import javax.inject.Inject

const val NEXT_QUESTION_PAGE = 0
const val QUESTIONNAIRE_COMPLETE_PAGE = 1
const val REWARD_PAGE = 2
const val SUBMIT_ERROR_PAGE = 3
const val TRANSACTION_ERROR_PAGE = 4

open class QuestionnaireViewModel(restoreState: Boolean, val navigator: Navigator) :
        QuestionnaireActions {

    @Inject
    lateinit var categoriesRepository: CategoriesRepository
    @Inject
    lateinit var analytics: Analytics

    var questionnaireProgress: ObservableInt = ObservableInt()
    var nextFragment: ObservableField<Fragment> = ObservableField()
    var currentPageState: Int
    val shouldFinishActivity = ObservableBoolean(false)

    init {
        KinitApplication.coreComponent.inject(this)
        currentPageState =
                when {
                    restoreState -> getPageFromState()
                    !categoriesRepository.isTaskComplete() -> NEXT_QUESTION_PAGE
                    else -> QUESTIONNAIRE_COMPLETE_PAGE
                }
        moveToNextPage(currentPageState)
    }

    override fun next() {
        moveToNextPage(
                if (!categoriesRepository.isTaskComplete()) {
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
        return categoriesRepository.getNumOfAnsweredQuestions()
    }

    protected fun questionIndex(): Int {
        return categoriesRepository.getNumOfAnsweredQuestions() - 1
    }

    fun onClose(){
        navigator.navigateToCategory(categoriesRepository.currentTaskInProgress?.category_id!!, true)
        shouldFinishActivity.set(true)
        categoriesRepository.currentTaskRepo?.resetTaskState()
        onCloseEvent()
    }

    protected fun moveToNextPage(pageState: Int) {
        currentPageState = pageState
        questionnaireProgress.set(
                ((categoriesRepository.getNumOfAnsweredQuestions().toDouble() / categoriesRepository.currentTaskRepo?.taskInProgress?.questions!!.size) * 100).toInt())
        nextFragment.set(getFragment())
    }

    open fun getFragment(): Fragment {
        val task = categoriesRepository.currentTaskRepo?.taskInProgress
        return when (currentPageState) {
            NEXT_QUESTION_PAGE -> {
                when {
                    task?.isQuiz()!! -> QuizFragment.newInstance(nextQuestionIndex())
                    task?.questions?.get(nextQuestionIndex())?.isTypeDualImage()!! -> QuestionDualImageFragment.newInstance(nextQuestionIndex())
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


    fun onCloseEvent() {
        val event: Events.Event
        val task = categoriesRepository.currentTaskInProgress
        if (categoriesRepository.isTaskComplete()) {
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
            categoriesRepository.getCurrentTaskState() == TaskState.SUBMITTED_SUCCESS_WAIT_FOR_REWARD -> REWARD_PAGE
            categoriesRepository.getCurrentTaskState() == TaskState.TRANSACTION_COMPLETED -> REWARD_PAGE
            categoriesRepository.getCurrentTaskState() == TaskState.TRANSACTION_ERROR -> TRANSACTION_ERROR_PAGE
            categoriesRepository.getCurrentTaskState() == TaskState.SUBMIT_ERROR_RETRY -> QUESTIONNAIRE_COMPLETE_PAGE
            categoriesRepository.getCurrentTaskState() == TaskState.SUBMIT_ERROR_NO_RETRY -> SUBMIT_ERROR_PAGE
            categoriesRepository.getCurrentTaskState() == TaskState.SUBMITTED -> SUBMIT_ERROR_PAGE
            else -> {
                if (!categoriesRepository.isTaskComplete())
                    NEXT_QUESTION_PAGE
                else QUESTIONNAIRE_COMPLETE_PAGE
            }
        }
    }
}
