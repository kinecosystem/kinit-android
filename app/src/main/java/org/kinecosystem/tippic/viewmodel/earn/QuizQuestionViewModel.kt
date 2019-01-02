package org.kinecosystem.tippic.viewmodel.earn

import android.databinding.ObservableBoolean
import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.analytics.Analytics
import org.kinecosystem.tippic.analytics.Events
import org.kinecosystem.tippic.model.earn.Answer
import org.kinecosystem.tippic.model.earn.Question
import org.kinecosystem.tippic.model.earn.Task
import org.kinecosystem.tippic.repository.CategoriesRepository
import org.kinecosystem.tippic.util.Scheduler
import org.kinecosystem.tippic.view.customView.QuizAnswerView
import org.kinecosystem.tippic.view.earn.QuestionnaireActions
import javax.inject.Inject

open class QuizQuestionViewModel(private var questionIndex: Int,
                                 private val questionnaireActions: QuestionnaireActions) {


    private val delay_show_correct_answer = 500L
    @Inject
    lateinit var scheduler: Scheduler
    @Inject
    lateinit var categoriesRepository: CategoriesRepository
    @Inject
    lateinit var analytics: Analytics

    private var questionObj: Question? = null
    var chosenAnswersIndexes: MutableList<Int> = mutableListOf()
    var chosenAnswers: MutableList<String> = mutableListOf()
    var question: String?
    var answers: List<Answer>?
    var reward: Int
    var clickable = ObservableBoolean(true)
    var showCorrect: ObservableBoolean = ObservableBoolean(false)

    var selectionListener: QuizAnswerView.OnSelectionListener = object : QuizAnswerView.OnSelectionListener {
        override fun onAnimComplete() {
            questionnaireActions.next()
        }

        override fun onAnswered(answer: Answer?) {
            clickable.set(false)
            answer?.let {
                chosenAnswers.add(it.id!!)
                categoriesRepository.currentTaskRepo?.setChosenAnswers(questionObj?.id!!, chosenAnswers)
                if (!isCorrect(it)) {
                    scheduler.scheduleOnMain({ showCorrect.set(true) }, delay_show_correct_answer)

                }
                analytics.logEvent(answerEvent(categoriesRepository.currentTaskInProgress))
            }
        }
    }


    init {
        TippicApplication.coreComponent.inject(this)
        questionObj = categoriesRepository.currentTaskInProgress?.questions?.get(questionIndex)
        question = questionObj?.text
        answers = questionObj?.answers
        reward = questionObj?.quiz_data?.reward ?: 0
    }

    private fun answerEvent(task: Task?): Events.Event? {
        return Events.Analytics.ClickAnswerButtonOnQuestionPage(
                if (chosenAnswers.size == 1) chosenAnswers[0] else chosenAnswers.toString(),
                if (chosenAnswersIndexes.size == 1) chosenAnswersIndexes[0] else -1,
                task?.provider?.name,
                task?.minToComplete,
                task?.kinReward,
                questionObj?.answers?.count(),
                task?.questions?.count(),
                questionObj?.id,
                questionIndex,
                questionObj?.type,
                categoriesRepository.currentCategoryTitle,
                task?.id,
                task?.title)
    }

    fun isCorrect(answer: Answer?): Boolean {
        answer?.let {
            return questionObj?.quiz_data?.answer_id == it.id
        }
        return false
    }

    fun onResume() {
        val task = categoriesRepository.currentTaskInProgress
        val event = Events.Analytics.ViewQuestionPage(task?.provider?.name,
                task?.minToComplete,
                task?.kinReward,
                task?.questions?.count(),
                questionObj?.id,
                questionIndex,
                questionObj?.type,
                categoriesRepository.currentCategoryTitle,
                task?.id,
                task?.title)
        analytics.logEvent(event)
    }
}

