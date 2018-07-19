package org.kinecosystem.kinit.view.earn

interface QuestionnaireActions : TransactionActions {
    fun next()
    fun submissionAnimComplete()
    fun submissionError()
}

interface TransactionActions {
    fun transactionError()
}