package org.kinecosystem.kinit.viewmodel.earn

import android.database.Observable
import android.databinding.Bindable
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.util.Log
import android.view.MotionEvent
import android.view.View
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.earn.Question
import org.kinecosystem.kinit.model.earn.Task
import org.kinecosystem.kinit.model.earn.tagsString
import org.kinecosystem.kinit.repository.TasksRepository
import org.kinecosystem.kinit.util.Scheduler
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

    private val ANSWER_ANIM_DURATION: Long = 1400
    private var question: Question?
    var chosenAnswers: MutableList<String> = mutableListOf()
    var imageUrls: MutableList<String> = mutableListOf()
    var questionText: String?
    var isAnswerSelected: MutableList<ObservableBoolean> = mutableListOf()
    var answerIndex = -1
    var clickable = ObservableBoolean(true)
    var touchX = ObservableField<Int>(0)
    var touchY = ObservableField<Int>(0)

    val onTouchListener:View.OnTouchListener = View.OnTouchListener { v, event ->
        when (event?.action) {
            MotionEvent.ACTION_DOWN ->
            {
                touchX.set(event.rawX.toInt())
                touchY.set(event.rawY.toInt())
                Log.d("###", "### down rawx:${event.rawX} x:${event.x}")
            }

        }

        v?.onTouchEvent(event) ?: true
    }


    init {
        KinitApplication.coreComponent.inject(this)
        question = questionnaireRepository.task?.questions?.get(questionIndex)
        questionText = question?.text
        for (answer in question?.answers!!) {
            answer.imageUrl?.let { imageUrls.add(it) }
            isAnswerSelected.add(ObservableBoolean(false))
        }
        Log.d("###", "#### load " + imageUrls)
    }

    fun onAnswered(view: View) {
        clickable.set(false)
        answerIndex = (view.tag as String).toInt()
        isAnswerSelected[answerIndex].set(true)
        question?.answers!![answerIndex].id?.let { chosenAnswers.add(it) }
        questionnaireRepository.setChosenAnswers(question?.id!!, chosenAnswers)
        scheduler.scheduleOnMain({
            questionnaireActions?.nextQuestion()
        }, ANSWER_ANIM_DURATION)
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
                if (chosenAnswers.size == 1) chosenAnswers[0] else chosenAnswers.toString(),
                answerIndex,
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

