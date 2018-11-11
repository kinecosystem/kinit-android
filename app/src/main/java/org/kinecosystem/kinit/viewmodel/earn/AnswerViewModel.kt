package org.kinecosystem.kinit.viewmodel.earn

import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.repository.CategoriesRepository
import org.kinecosystem.kinit.view.earn.QuestionnaireActions
import javax.inject.Inject

class AnswerViewModel(questionIndex: Int,
                      private val questionnaireActions: QuestionnaireActions) {


    @Inject
    lateinit var categoriesRepository: CategoriesRepository

    var answer: String? = ""

    init {
        KinitApplication.coreComponent.inject(this)
        answer = categoriesRepository.currentTaskInProgress?.questions?.get(questionIndex)?.quiz_data?.explanation
    }


    fun onNext() {
        questionnaireActions.next()
    }


}

