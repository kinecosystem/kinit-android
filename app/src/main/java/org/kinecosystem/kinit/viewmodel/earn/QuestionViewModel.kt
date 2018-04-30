package org.kinecosystem.kinit.viewmodel.earn

import org.kinecosystem.kinit.CoreComponentsProvider
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.earn.Answer
import org.kinecosystem.kinit.model.earn.Question
import org.kinecosystem.kinit.model.earn.tagsString
import org.kinecosystem.kinit.view.customView.AnswersGridLayout
import org.kinecosystem.kinit.view.earn.QuestionnaireActions

open class QuestionViewModel(
        coreComponents: CoreComponentsProvider, private var questionIndex: Int,
        questionnaireActions: QuestionnaireActions) {

    private val repo = coreComponents.questionnaireRepo()
    private val analytics = coreComponents.analytics()
    private val questionObj: Question? = coreComponents.questionnaireRepo().task?.questions?.get(questionIndex)
    private var answered = false

    var questionType = questionObj?.type ?: Question.QuestionType.TEXT.type
    var isImageTextAnswer = questionType == Question.QuestionType.TEXT_IMAGE.type
    var isAlignedLeft = questionType == Question.QuestionType.TEXT_EMOJI.type
    var question: String? = questionObj?.text
    var answers: List<Answer>? = questionObj?.answers

    fun getColumnCount(): Int {
        if (isImageTextAnswer) return 2; return 1
    }

    fun isHorizontal(): Boolean {
        if (isImageTextAnswer) return true; return false
    }

    var onAnswer: AnswersGridLayout.OnAnswerListener = AnswersGridLayout.OnAnswerListener { answer ->
        if (!answered) {
            answered = true
            val task = repo.task
            repo.setChosenAnswer(questionObj?.id!!, answer.id!!)
            questionnaireActions.nextQuestion()

            val event = Events.Analytics.ClickAnswerButtonOnQuestionPage(answer?.id,
                    questionObj.answers?.indexOf(answer),
                    task?.provider?.name,
                    task?.minToComplete,
                    task?.kinReward,
                    questionObj.answers?.count(),
                    task?.questions?.count(),
                    questionObj.id,
                    questionIndex,
                    questionObj.type,
                    task?.tagsString(),
                    task?.id,
                    task?.type)
            analytics.logEvent(event)
            return@OnAnswerListener true
        }
        return@OnAnswerListener false
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

