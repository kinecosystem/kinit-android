package org.kinecosystem.kinit.view.earn

interface QuestionnaireActions : TransactionActions {
    fun nextQuestion()
    fun submissionComplete()

    fun submissionError()
}

interface TransactionActions {
    fun transactionError()
}