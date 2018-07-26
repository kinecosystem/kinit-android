package org.kinecosystem.kinit.model

class TaskState {
    companion object {
        // new task
        const val IDLE = 0
        // user has clicked on start or continue
        const val IN_PROGRESS = 8
        // user finished the task and we submitted to server
        const val SUBMITTED = 7
        // task submission was successful
        const val SUBMITTED_SUCCESS_WAIT_FOR_REWARD = 1
        // there was an error when submitting the task
        // user can retry later
        const val SUBMIT_ERROR_RETRY = 2
        // there was an unrecoverable submission error
        const val SUBMIT_ERROR_NO_RETRY = 3
        // there was a transaction error
        const val TRANSACTION_ERROR = 4
        // user received their Kin!
        const val TRANSACTION_COMPLETED = 6
    }
}