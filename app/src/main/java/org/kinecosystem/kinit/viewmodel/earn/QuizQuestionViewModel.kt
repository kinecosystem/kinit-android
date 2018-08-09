package org.kinecosystem.kinit.viewmodel.earn

import android.databinding.ObservableBoolean
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.earn.Answer
import org.kinecosystem.kinit.model.earn.Question
import org.kinecosystem.kinit.model.earn.Task
import org.kinecosystem.kinit.model.earn.tagsString
import org.kinecosystem.kinit.repository.TasksRepository
import org.kinecosystem.kinit.util.Scheduler
import org.kinecosystem.kinit.view.customView.QuizAnswerView
import org.kinecosystem.kinit.view.earn.QuestionnaireActions
import javax.inject.Inject

open class QuizQuestionViewModel(private var questionIndex: Int,
                                 private val questionnaireActions: QuestionnaireActions) {


    private val delay_show_correct_answer = 500L
    @Inject
    lateinit var scheduler: Scheduler
    @Inject
    lateinit var taskRepository: TasksRepository
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
                taskRepository.setChosenAnswers(questionObj?.id!!, chosenAnswers)
                if (!isCorrect(it)) {
                    scheduler.scheduleOnMain({ showCorrect.set(true) }, delay_show_correct_answer)

                }
                analytics.logEvent(answerEvent(taskRepository.task))
            }
        }
    }


    init {
        KinitApplication.coreComponent.inject(this)
        questionObj = taskRepository.task?.questions?.get(questionIndex)
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
                task?.tagsString(),
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
        val task = taskRepository.task
        val event = Events.Analytics.ViewQuestionPage(task?.provider?.name,
                task?.minToComplete,
                task?.kinReward,
                task?.questions?.count(),
                questionObj?.id,
                questionIndex,
                questionObj?.type,
                task?.tagsString(),
                task?.id,
                task?.title)
        analytics.logEvent(event)
    }
}

