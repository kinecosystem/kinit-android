package org.kinecosystem.kinit.viewmodel.earn

import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.support.v4.app.Fragment
import org.kinecosystem.kinit.CoreComponentsProvider
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.TaskState
import org.kinecosystem.kinit.model.earn.tagsString
import org.kinecosystem.kinit.view.earn.*

private const val NEXT_QUESTION_PAGE = 0
private const val QUESTIONNAIRE_COMPLETE_PAGE = 1
private const val REWARD_PAGE = 2
private const val SUBMIT_ERROR_PAGE = 3
private const val TRANSACTION_ERROR_PAGE = 4


open class QuestionnaireViewModel(private val coreComponents: CoreComponentsProvider, restoreState: Boolean) :
    QuestionnaireActions {

    private val repo = coreComponents.questionnaireRepo()
    private val task = repo.task
    var questionnaireProgress: ObservableInt = ObservableInt()
    var nextFragment: ObservableField<Fragment> = ObservableField()

    private var hasNext: Boolean = false
        get() = !repo.isQuestionnaireComplete()

    private var nextQuestionIndex: Int = 0
        get() = repo.getNumOfAnsweredQuestions()

    private var currentPage: Int =
        when {
            restoreState -> getPageFromState()
            hasNext -> NEXT_QUESTION_PAGE
            else -> QUESTIONNAIRE_COMPLETE_PAGE
        }

    init {
        moveToNextPage(currentPage)
    }

    override fun nextQuestion() {
        moveToNextPage(
            if (hasNext) {
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

    override fun submissionComplete() {
        moveToNextPage(REWARD_PAGE)
    }

    private fun moveToNextPage(page: Int) {
        currentPage = page
        questionnaireProgress.set(
            ((repo.getNumOfAnsweredQuestions().toDouble() / task?.questions!!.size) * 100).toInt())
        nextFragment.set(getFragment())
    }

    private fun getFragment(): Fragment {

        return when (currentPage) {
            NEXT_QUESTION_PAGE -> QuestionFragment.newInstance(nextQuestionIndex)
            QUESTIONNAIRE_COMPLETE_PAGE -> QuestionnaireCompleteFragment.newInstance()
            REWARD_PAGE -> QuestionnaireRewardFragment.newInstance()
            TRANSACTION_ERROR_PAGE -> TaskErrorFragment.newInstance(
                TaskErrorFragment.ERROR_TRANSACTION)
            else -> TaskErrorFragment.newInstance(TaskErrorFragment.ERROR_SUBMIT)
        }
    }

    fun onBackPressed() {
        val event: Events.Event
        if (repo.isQuestionnaireComplete()) {
            event = Events.Analytics.ClickCloseButtonOnRewardPage(task?.provider?.name,
                task?.minToComplete,
                task?.kinReward,
                task?.tagsString(),
                task?.id,
                task?.title,
                task?.type)
        } else {
            val question = task?.questions?.get(nextQuestionIndex)
            event = Events.Analytics.ClickCloseButtonOnQuestionPage(task?.provider?.name,
                task?.minToComplete,
                task?.kinReward,
                question?.answers?.count(),
                task?.questions?.count(),
                question?.id,
                nextQuestionIndex + 1,
                question?.type,
                task?.tagsString(),
                task?.id,
                task?.title)
        }
        coreComponents.analytics().logEvent(event)
    }


    private fun getPageFromState(): Int {
        return when {
            repo.taskState == TaskState.SUBMITTED_SUCCESS_WAIT_FOR_REWARD -> REWARD_PAGE
            repo.taskState == TaskState.TRANSACTION_COMPLETED -> REWARD_PAGE
            repo.taskState == TaskState.TRANSACTION_ERROR -> TRANSACTION_ERROR_PAGE
            repo.taskState == TaskState.SUBMIT_ERROR_RETRY -> QUESTIONNAIRE_COMPLETE_PAGE
            repo.taskState == TaskState.SUBMIT_ERROR_NO_RETRY -> SUBMIT_ERROR_PAGE
            else -> {
                if (hasNext)
                    NEXT_QUESTION_PAGE
                else QUESTIONNAIRE_COMPLETE_PAGE
            }
        }
    }
}
