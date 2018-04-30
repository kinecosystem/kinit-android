package org.kinecosystem.kinit.view.earn

interface QuestionnaireActions {
    fun nextQuestion()
    fun submissionComplete()
    fun transactionError()
    fun submissionError()
}