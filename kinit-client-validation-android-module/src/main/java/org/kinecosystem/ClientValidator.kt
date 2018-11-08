package org.kinecosystem

import android.app.Activity

class ClientValidator(activity: Activity){
    private val client = null

    interface OnValidationResult {
        fun isValid(s: String)
        fun isInvalid(s: String)
    }

    fun validateClient(token: String, callback: ClientValidator.OnValidationResult){}
}

