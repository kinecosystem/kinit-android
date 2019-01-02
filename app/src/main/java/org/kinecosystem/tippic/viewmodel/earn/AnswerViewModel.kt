package org.kinecosystem.tippic.viewmodel.earn

import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.repository.CategoriesRepository
import org.kinecosystem.tippic.view.earn.QuestionnaireActions
import javax.inject.Inject

class AnswerViewModel(questionIndex: Int,
                      private val questionnaireActions: QuestionnaireActions) {


    @Inject
    lateinit var categoriesRepository: CategoriesRepository

    var answer: String? = ""

    init {
        TippicApplication.coreComponent.inject(this)
        answer = categoriesRepository.currentTaskInProgress?.questions?.get(questionIndex)?.quiz_data?.explanation
    }


    fun onNext() {
        questionnaireActions.next()
    }


}

