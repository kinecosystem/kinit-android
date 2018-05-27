package org.kinecosystem.kinit.viewmodel.earn

import android.databinding.ObservableInt
import org.kinecosystem.kinit.CoreComponentsProvider
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.earn.Answer
import org.kinecosystem.kinit.model.earn.Question
import org.kinecosystem.kinit.model.earn.Task
import org.kinecosystem.kinit.model.earn.tagsString
import org.kinecosystem.kinit.view.customView.AnswersGridLayout
import org.kinecosystem.kinit.view.earn.QuestionnaireActions

open class QuestionViewModel(val coreComponents: CoreComponentsProvider, private var questionIndex: Int,
                             private val questionnaireActions: QuestionnaireActions) {

    private val repo = coreComponents.questionnaireRepo()
    private val analytics = coreComponents.analytics()
    private val questionObj: Question? = coreComponents.questionnaireRepo().task?.questions?.get(questionIndex)
    private var questionAnswered = false

    private var chosenAnswersIndexes: MutableList<Int> = mutableListOf()
    private var chosenAnswers: MutableList<String> = mutableListOf()
    val imageUrl = questionObj?.image_url
    var chosenAnswersCount: ObservableInt = ObservableInt(chosenAnswers.size)
    var questionType = questionObj?.type ?: Question.QuestionType.TEXT.type
    private var isImageTextAnswer = questionType == Question.QuestionType.TEXT_IMAGE.type
    var isMultipleAnswers = questionType == Question.QuestionType.TEXT_MULTIPLE.type
    var isHeaderImageAvailable = questionObj?.image_url?.isNotBlank() ?: false
    var isHeaderImageAndMultiple = isHeaderImageAvailable && isMultipleAnswers

    var isAlignedLeft = questionType == Question.QuestionType.TEXT_EMOJI.type
    var question: String? = questionObj?.text
    var answers: List<Answer>? = questionObj?.answers
    var isFullWidth = isMultipleAnswers
    var isHorizontal = isImageTextAnswer
    var columnCount = if (isImageTextAnswer) 2 else 1

    private fun answerEvent(task: Task?): Events.Event? {
        return Events.Analytics.ClickAnswerButtonOnQuestionPage(
                chosenAnswers.toString(),
                -1,
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
                task?.type)
    }

    fun onNext() {
        val answerId = questionObj?.id ?: ""
        if (answerId.isNotBlank() and !questionAnswered and (chosenAnswersCount.get() > 0)) {
            questionAnswered = true
            val task = repo.task
            repo.setChosenAnswers(answerId, chosenAnswers)
            questionnaireActions.nextQuestion()
            analytics.logEvent(answerEvent(task))
        }
    }

    var onAnswer: AnswersGridLayout.OnAnswerSelectedListener = AnswersGridLayout.OnAnswerSelectedListener { answer ->
        val answerId = answer.id ?: ""
        val questionId = questionObj?.id ?: ""

        if (answerId.isBlank() || questionId.isBlank()) return@OnAnswerSelectedListener false
        when {
            questionAnswered and !isMultipleAnswers -> return@OnAnswerSelectedListener true
            !questionAnswered and !isMultipleAnswers -> {
                val task = repo.task
                chosenAnswers.add(answerId)
                chosenAnswersIndexes.add(questionObj?.answers?.indexOf(answer) ?: -1)
                repo.setChosenAnswers(questionId, chosenAnswers)
                questionnaireActions.nextQuestion()
                analytics.logEvent(answerEvent(task))
                questionAnswered = true
                return@OnAnswerSelectedListener true
            }
            !questionAnswered and isMultipleAnswers and !chosenAnswers.contains(answerId) -> {
                chosenAnswers.add(answerId)
                chosenAnswersIndexes.add(questionObj?.answers?.indexOf(answer) ?: -1)
                chosenAnswersCount.set(chosenAnswers.size)
                return@OnAnswerSelectedListener true
            }
            !questionAnswered and isMultipleAnswers and chosenAnswers.contains(answerId) -> {
                chosenAnswersIndexes.removeAt(chosenAnswers.indexOf(answerId))
                chosenAnswers.remove(answerId)
                chosenAnswersCount.set(chosenAnswers.size)
                return@OnAnswerSelectedListener false
            }
            else -> return@OnAnswerSelectedListener false
        }
    }

    fun onResume() {
        val task = repo.task
        val event = Events.Analytics.ViewQuestionPage(task?.provider?.name,
                task?.minToComplete,
                task?.kinReward,
                task?.questions?.count(),
                questionObj?.id,
                questionIndex,
                questionObj?.type,
                task?.tagsString(),
                task?.id,
                task?.type)
        analytics.logEvent(event)
    }
}

