package org.kinecosystem.kinit.viewmodel.earn

import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.repository.TasksRepository
import org.kinecosystem.kinit.util.Scheduler
import org.kinecosystem.kinit.view.earn.QuestionnaireActions
import javax.inject.Inject

class AnswerViewModel(questionIndex: Int,
                      private val questionnaireActions: QuestionnaireActions) {


    @Inject
    lateinit var scheduler: Scheduler
    @Inject
    lateinit var questionnaireRepository: TasksRepository
    @Inject
    lateinit var analytics: Analytics

    var answer: String? = ""

    init {
        KinitApplication.coreComponent.inject(this)
        answer = questionnaireRepository.task?.questions?.get(questionIndex)?.quiz_data?.explanation
    }


    fun onNext() {
        questionnaireActions.next()
    }


}

