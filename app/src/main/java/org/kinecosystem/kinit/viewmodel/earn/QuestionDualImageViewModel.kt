package org.kinecosystem.kinit.viewmodel.earn

import android.databinding.ObservableBoolean
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.earn.Answer
import org.kinecosystem.kinit.model.earn.Question
import org.kinecosystem.kinit.model.earn.Task
import org.kinecosystem.kinit.model.earn.tagsString
import org.kinecosystem.kinit.repository.CategoriesRepository
import org.kinecosystem.kinit.repository.TasksRepository
import org.kinecosystem.kinit.util.Scheduler
import org.kinecosystem.kinit.view.customView.AnswerSelectedOverView.OnSelectionListener
import org.kinecosystem.kinit.view.earn.QuestionnaireActions
import javax.inject.Inject

class QuestionDualImageViewModel(private var questionIndex: Int,
                                 private val questionnaireActions: QuestionnaireActions?) {
    @Inject
    lateinit var scheduler: Scheduler
    @Inject
    lateinit var categoriesRepository: CategoriesRepository
    @Inject
    lateinit var analytics: Analytics

    private val POST_ANSWER_DELAY: Long = 400
    private var question: Question?
    var imageUrls: MutableList<String> = mutableListOf()
    var questionText: String?
    var answers: List<Answer>?
    var onSelectionComplete = ObservableBoolean(false)

    var selectionListener: OnSelectionListener = object : OnSelectionListener {
        override fun onAnimComplete() {
            scheduler.scheduleOnMain({
                questionnaireActions?.next()
            }, POST_ANSWER_DELAY)
        }

        override fun onSelected(answer: Answer) {
            onSelectionComplete.set(true)
            onAnswered(answer)
        }
    }

    init {
        KinitApplication.coreComponent.inject(this)
<<<<<<< HEAD
        question = taskRepository.taskInProgress?.questions?.get(questionIndex)
=======
        question = categoriesRepository.currentTaskInProgress?.questions?.get(questionIndex)
>>>>>>> taskcategories
        answers = question?.answers
        questionText = question?.text
        question?.answers?.forEach { answer ->
            answer.imageUrl?.let { imageUrls.add(it) }
        }
    }

    fun onAnswered(answer: Answer) {
        val answeredId = answer.id ?: ""
<<<<<<< HEAD
        question?.id?.let { taskRepository.setChosenAnswers(it, listOf(answeredId)) }
        analytics.logEvent(answerEvent(taskRepository.taskInProgress, answeredId))
    }

    fun onResume() {
        val task = taskRepository.taskInProgress
=======
        question?.id?.let { categoriesRepository.currentTaskRepo?.setChosenAnswers(it, listOf(answeredId)) }
        analytics.logEvent(answerEvent(categoriesRepository.currentTaskInProgress, answeredId))
    }

    fun onResume() {
        val task = categoriesRepository.currentTaskInProgress
>>>>>>> taskcategories
        val event = Events.Analytics.ViewQuestionPage(task?.provider?.name,
                task?.minToComplete,
                task?.kinReward,
                task?.questions?.count(),
                question?.id,
                questionIndex,
                question?.type,
                task?.tagsString(),
                task?.id,
                task?.title)
        analytics.logEvent(event)
    }

    private fun answerEvent(task: Task?, answerId: String): Events.Event? {
        return Events.Analytics.ClickAnswerButtonOnQuestionPage(
                answerId,
                -1,
                task?.provider?.name,
                task?.minToComplete,
                task?.kinReward,
                question?.answers?.count(),
                task?.questions?.count(),
                question?.id,
                questionIndex,
                question?.type,
                task?.tagsString(),
                task?.id,
                task?.title)
    }
}

