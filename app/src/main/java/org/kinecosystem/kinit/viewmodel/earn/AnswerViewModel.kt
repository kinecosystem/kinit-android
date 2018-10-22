package org.kinecosystem.kinit.viewmodel.earn

import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.repository.CategoriesRepository
import org.kinecosystem.kinit.repository.TasksRepository
import org.kinecosystem.kinit.util.Scheduler
import org.kinecosystem.kinit.view.earn.QuestionnaireActions
import javax.inject.Inject

class AnswerViewModel(questionIndex: Int,
                      private val questionnaireActions: QuestionnaireActions) {


    @Inject
    lateinit var scheduler: Scheduler
    @Inject
    lateinit var categoriesRepository: CategoriesRepository
    @Inject
    lateinit var analytics: Analytics

    var answer: String? = ""

    init {
        KinitApplication.coreComponent.inject(this)
<<<<<<< HEAD
        answer = questionnaireRepository.taskInProgress?.questions?.get(questionIndex)?.quiz_data?.explanation
=======
        answer = categoriesRepository.currentTaskInProgress?.questions?.get(questionIndex)?.quiz_data?.explanation
>>>>>>> taskcategories
    }


    fun onNext() {
        questionnaireActions.next()
    }


}

