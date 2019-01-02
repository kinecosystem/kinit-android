package org.kinecosystem.tippic.viewmodel.earn

import android.databinding.ObservableInt
import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.analytics.Analytics
import org.kinecosystem.tippic.analytics.Events
import org.kinecosystem.tippic.model.earn.Answer
import org.kinecosystem.tippic.model.earn.Question
import org.kinecosystem.tippic.model.earn.Task
import org.kinecosystem.tippic.repository.CategoriesRepository
import org.kinecosystem.tippic.util.Scheduler
import org.kinecosystem.tippic.view.adapter.AnswersListAdapter
import org.kinecosystem.tippic.view.earn.QuestionnaireActions
import javax.inject.Inject

open class QuestionViewModel(private var questionIndex: Int,
                             private val questionnaireActions: QuestionnaireActions) {


    @Inject
    lateinit var scheduler: Scheduler
    @Inject
    lateinit var categoriesRepository: CategoriesRepository
    @Inject
    lateinit var analytics: Analytics

    private var questionObj: Question?
    private var questionAnswered = false
    var chosenAnswersIndexes: MutableList<Int> = mutableListOf()
    var chosenAnswers: MutableList<String> = mutableListOf()
    var chosenAnswersCount: ObservableInt = ObservableInt(chosenAnswers.size)
    var question: String?
    var answers: List<Answer>?

    var questionType: String
    var isMultipleAnswers: Boolean
    private var isImageTextAnswer: Boolean

    val imageUrl: String?
    var isHeaderImageAvailable: Boolean
    var isFullWidth: Boolean
    var columnCount: Int

    init {
        TippicApplication.coreComponent.inject(this)
        questionObj = categoriesRepository.currentTaskInProgress?.questions?.get(questionIndex)
        question = questionObj?.text
        answers = questionObj?.answers

        questionType = questionObj?.type ?: Question.QuestionType.TEXT.type
        isMultipleAnswers = questionType == Question.QuestionType.TEXT_MULTIPLE.type
        isImageTextAnswer = questionType == Question.QuestionType.TEXT_IMAGE.type

        imageUrl = questionObj?.image_url
        isHeaderImageAvailable = questionObj?.image_url?.isNotBlank() ?: false
        isFullWidth = isMultipleAnswers
        columnCount = if (isImageTextAnswer) 2 else 1
    }

    private fun answerEvent(task: Task?): Events.Event? {
        return Events.Analytics.ClickAnswerButtonOnQuestionPage(
                if (chosenAnswers.size == 1) chosenAnswers[0] else chosenAnswers.toString(),
                if (chosenAnswers.size == 1) chosenAnswersIndexes[0] else -1,
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

    fun onNext() {
        val answerId = questionObj?.id ?: ""
        if (answerId.isNotBlank() and !questionAnswered and (chosenAnswersCount.get() > 0)) {
            questionAnswered = true
            categoriesRepository.currentTaskRepo?.setChosenAnswers(answerId, chosenAnswers)
            questionnaireActions.next()
            analytics.logEvent(answerEvent(categoriesRepository.currentTaskInProgress))
        }
    }

    val onAnswer = object : AnswersListAdapter.OnAnswerSelectedListener {
        override fun answerSelected(answer: Answer?): Boolean {
            val answerId = answer?.id ?: ""
            val questionId = questionObj?.id ?: ""

            when {
                answerId.isBlank() or questionId.isBlank() -> return false
                questionAnswered and !isMultipleAnswers -> return false
                !questionAnswered and !isMultipleAnswers -> {
                    chosenAnswers.add(answerId)
                    chosenAnswersIndexes.add(questionObj?.answers?.indexOf(answer) ?: -1)
                    chosenAnswersCount.set(chosenAnswers.size)
                    categoriesRepository.currentTaskRepo?.setChosenAnswers(questionId, chosenAnswers)
                    scheduler.scheduleOnMain({
                        questionnaireActions.next()
                    }, 200)
                    analytics.logEvent(answerEvent(categoriesRepository.currentTaskInProgress))
                    questionAnswered = true
                    return true
                }

                !questionAnswered and isMultipleAnswers and !chosenAnswers.contains(answerId) -> {
                    chosenAnswers.add(answerId)
                    chosenAnswersIndexes.add(questionObj?.answers?.indexOf(answer) ?: -1)
                    chosenAnswersCount.set(chosenAnswers.size)
                    return true
                }

                !questionAnswered and isMultipleAnswers and chosenAnswers.contains(answerId) -> {
                    chosenAnswersIndexes.removeAt(chosenAnswers.indexOf(answerId))
                    chosenAnswers.remove(answerId)
                    chosenAnswersCount.set(chosenAnswers.size)
                    return false
                }

                else -> return false
            }
        }

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

