package org.kinecosystem.kinit.network

import com.google.gson.annotations.SerializedName

const val ERROR_NO_INTERNET = 1
const val ERROR_NO_PUBLIC_ADDRESS = 2
const val ERROR_EMPTY_RESPONSE = 3
const val ERROR_FAILED_RESPONSE = 4
const val ERROR_INVALID_DATA = 5
const val USER_HEADER_KEY = "X-USERID"

data class StatusResponse(@SerializedName("status") val status: String)