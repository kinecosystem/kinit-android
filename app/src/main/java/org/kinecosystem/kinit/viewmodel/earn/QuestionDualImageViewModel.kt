package org.kinecosystem.kinit.viewmodel.earn

import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.earn.Answer
import org.kinecosystem.kinit.model.earn.Question
import org.kinecosystem.kinit.model.earn.Task
import org.kinecosystem.kinit.model.earn.tagsString
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
    lateinit var questionnaireRepository: TasksRepository
    @Inject
    lateinit var analytics: Analytics

    private val POST_ANSWER_DELAY: Long = 400
    private var question: Question? = null
    var chosenAnswers: MutableList<String> = mutableListOf()
    var imageUrls: MutableList<String> = mutableListOf()
    var selectionListeners: MutableList<OnSelectionListener> = mutableListOf()
    var questionText: String?

    init {
        KinitApplication.coreComponent.inject(this)
        question = questionnaireRepository.task?.questions?.get(questionIndex)
        questionText = question?.text
        for (answer in question?.answers!!) {
            answer.imageUrl?.let { imageUrls.add(it) }
            selectionListeners.add(object : OnSelectionListener {
                override fun onSelected() {
                    val index = selectionListeners.indexOf(this)
                    onAnswered(question?.answers!![index])
                }
            })
        }
    }

    fun onAnswered(answer: Answer) {
        answer.id?.let { chosenAnswers.add(it) }
        questionnaireRepository.setChosenAnswers(question?.id!!, chosenAnswers)
        scheduler.scheduleOnMain({
            questionnaireActions?.nextQuestion()
        }, POST_ANSWER_DELAY)
        analytics.logEvent(answerEvent(questionnaireRepository.task))
    }

    fun onResume() {
        val task = questionnaireRepository.task
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

    private fun answerEvent(task: Task?): Events.Event? {
        return Events.Analytics.ClickAnswerButtonOnQuestionPage(
                chosenAnswers[0],
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

