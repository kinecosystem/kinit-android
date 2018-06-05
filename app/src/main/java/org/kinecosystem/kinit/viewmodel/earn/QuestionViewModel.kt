package org.kinecosystem.kinit.viewmodel.earn

import android.databinding.ObservableInt
import org.kinecosystem.kinit.CoreComponentsProvider
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.earn.Answer
import org.kinecosystem.kinit.model.earn.Question
import org.kinecosystem.kinit.model.earn.Task
import org.kinecosystem.kinit.model.earn.tagsString
import org.kinecosystem.kinit.view.adapter.AnswersListAdapter
import org.kinecosystem.kinit.view.earn.QuestionnaireActions

open class QuestionViewModel(val coreComponents: CoreComponentsProvider, private var questionIndex: Int,
                             private val questionnaireActions: QuestionnaireActions) {

    private val repo = coreComponents.questionnaireRepo()
    private val analytics = coreComponents.analytics()
    private val questionObj: Question? = coreComponents.questionnaireRepo().task?.questions?.get(questionIndex)
    private var questionAnswered = false
    var chosenAnswersIndexes: MutableList<Int> = mutableListOf()
    var chosenAnswers: MutableList<String> = mutableListOf()
    var chosenAnswersCount: ObservableInt = ObservableInt(chosenAnswers.size)
    var question: String? = questionObj?.text
    var answers: List<Answer>? = questionObj?.answers

    var questionType = questionObj?.type ?: Question.QuestionType.TEXT.type
    var isMultipleAnswers = questionType == Question.QuestionType.TEXT_MULTIPLE.type
    var isImageTextAnswer = questionType == Question.QuestionType.TEXT_IMAGE.type

    val imageUrl = questionObj?.image_url
    var isHeaderImageAvailable = questionObj?.image_url?.isNotBlank() ?: false
    var isFullWidth = isMultipleAnswers
    val columnCount = if (isImageTextAnswer) 2 else 1

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

    val onAnswer = object : AnswersListAdapter.OnAnswerSelectedListener {
        override fun answerSelected(answer: Answer?): Boolean {
            val answerId = answer?.id ?: ""
            val questionId = questionObj?.id ?: ""

            when {
                answerId.isBlank() or questionId.isBlank() -> return false
                questionAnswered and !isMultipleAnswers -> return false
                !questionAnswered and !isMultipleAnswers -> {
                    val task = repo.task
                    chosenAnswers.add(answerId)
                    chosenAnswersIndexes.add(questionObj?.answers?.indexOf(answer) ?: -1)
                    chosenAnswersCount.set(chosenAnswers.size)
                    repo.setChosenAnswers(questionId, chosenAnswers)
                    coreComponents.scheduler().scheduleOnMain({
                        questionnaireActions.nextQuestion()
                    }, 200)
                    analytics.logEvent(answerEvent(task))
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

