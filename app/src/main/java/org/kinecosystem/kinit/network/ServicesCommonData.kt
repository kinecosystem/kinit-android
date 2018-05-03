package org.kinecosystem.kinit.network

import com.google.gson.annotations.SerializedName

const val ERROR_NO_INTERNET = 1
const val ERROR_NO_PUBLIC_ADDRESS = 2
const val ERROR_EMPTY_RESPONSE = 3
const val ERROR_FAILED_RESPONSE = 4
const val ERROR_INVALID_DATA = 5
const val USER_HEADER_KEY = "X-USERID"

data class Config(@SerializedName("auth_token_enabled") val auth_token_enabled: Boolean,
    @SerializedName("p2p_enabled") val p2p_enabled: Boolean,
    @SerializedName("p2p_max_kin") val p2p_max_kin: String,
    @SerializedName("p2p_min_kin") val p2p_min_kin: String,
    @SerializedName("phone_verification_enabled") val phone_verification_enabled: Boolean,
    @SerializedName("tos") val tos: String)

data class StatusResponse(@SerializedName("status") val status: String,
    @SerializedName("config") val config: Config)