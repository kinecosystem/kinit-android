package org.kinecosystem.kinit.view.earn

interface QuestionnaireActions : TransactionActions {
    fun nextQuestion()
    fun submissionAnimComplete()
    fun submissionError()
}

interface TransactionActions {
    fun transactionError()
}