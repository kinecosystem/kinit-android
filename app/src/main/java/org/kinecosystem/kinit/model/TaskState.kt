package org.kinecosystem.kinit.model

class TaskState {
    companion object {
        const val INITIAL = 0 // questionnaire maybe started  started or started
        const val SUBMITTED_SUCCESS_WAIT_FOR_REWARD = 1
        const val SUBMIT_ERROR_RETRY = 2
        const val SUBMIT_ERROR_NO_RETRY = 3
        const val TRANSACTION_ERROR = 4
        const val TRANSACTION_COMPLETED = 6
    }
}