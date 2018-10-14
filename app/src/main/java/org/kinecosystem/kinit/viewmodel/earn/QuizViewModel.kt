package org.kinecosystem.kinit.viewmodel.earn

import android.support.v4.app.Fragment
import org.kinecosystem.kinit.model.TaskState
import org.kinecosystem.kinit.view.earn.*

const val SHOW_ANSWER_PAGE = 100

class QuizViewModel(taskId:String, restoreState: Boolean) : QuestionnaireViewModel(taskId, restoreState),
        QuestionnaireActions {


    private var showAnswer: Boolean = false

    init {
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
                if (showAnswer) {
                    SHOW_ANSWER_PAGE
                } else if (!taskRepository.isTaskComplete()) {
                    NEXT_QUESTION_PAGE
                } else QUESTIONNAIRE_COMPLETE_PAGE
        )
    }

    override fun getFragment(): Fragment {

        return when (currentPageState) {
            SHOW_ANSWER_PAGE -> {
                showAnswer = false
                AnswerExplainedFragment.newInstance(questionIndex())
            }
            NEXT_QUESTION_PAGE -> {
                showAnswer = true
                QuizFragment.newInstance(nextQuestionIndex())
            }
            QUESTIONNAIRE_COMPLETE_PAGE -> QuestionnaireCompleteFragment.newInstance()
            REWARD_PAGE -> TaskRewardFragment.newInstance()
            TRANSACTION_ERROR_PAGE -> TaskErrorFragment.newInstance(
                    TaskErrorFragment.ERROR_TRANSACTION)
            else -> TaskErrorFragment.newInstance(TaskErrorFragment.ERROR_SUBMIT)
        }
    }
}
