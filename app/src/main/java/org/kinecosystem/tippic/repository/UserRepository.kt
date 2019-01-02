package org.kinecosystem.tippic.repository

import android.util.Log
import org.kinecosystem.tippic.model.user.UserInfo
import java.util.*


private const val USER_ID_KEY = "user_id"
private const val IS_REGISTERED_KEY = "is_registered"
private const val IS_FRESH_INSTALL = "is_fresh_install"

private const val FCM_TOKEN_SENT_KEY = "token_sent"
private const val USER_CACHE_NAME = "tippic.app.user"
private const val TOS = "tos"
private const val PHONE_VERIFICATION_ENABLED = "PHONE_VERIFICATION_ENABLED"
private const val PHONE_VERIFIED = "PHONE_VERIFIED"
private const val FIRST_TIME_USER = "FIRST_TIME_USER"
private const val AUTH_TOKEN = "authToken"
private const val RESTORE_HINTS = "RESTORE_HINTS"
private const val TOS_DEFAULT = "http://www.kinitapp.com/terms-and-privacy-policy"
private const val FORCE_UPDATE = "FORCE_UPDATE"
private const val IS_UPDATE_AVAILABLE = "IS_UPDATE_AVAILABLE"


class UserRepository(dataStoreProvider: DataStoreProvider) {
    private val userCache: DataStore = dataStoreProvider.dataStore(USER_CACHE_NAME)
    var userInfo: UserInfo
        private set

    var fcmTokenSent: Boolean
        set(tokenSent) = userCache.putBoolean(FCM_TOKEN_SENT_KEY, tokenSent)
        get() = userCache.getBoolean(FCM_TOKEN_SENT_KEY, false)

    var isRegistered: Boolean
        set(registered) = userCache.putBoolean(IS_REGISTERED_KEY, registered)
        get() = userCache.getBoolean(IS_REGISTERED_KEY, false)

     var isUpdateAvailable: Boolean
        set(value) = userCache.putBoolean(IS_UPDATE_AVAILABLE, value)
        get() = userCache.getBoolean(IS_UPDATE_AVAILABLE, false)

    var forceUpdate: Boolean
        set(value) = userCache.putBoolean(FORCE_UPDATE, value)
        get() = userCache.getBoolean(FORCE_UPDATE, false)

    var isPhoneVerified: Boolean
        set(phoneVerified) = userCache.putBoolean(PHONE_VERIFIED, phoneVerified)
        get() = userCache.getBoolean(PHONE_VERIFIED, false)

    var isFreshInstall: Boolean
        set(freshInstall) = userCache.putBoolean(IS_FRESH_INSTALL, freshInstall)
        get() = userCache.getBoolean(IS_FRESH_INSTALL, true)

    var tos: String
        set(tos) = userCache.putString(TOS, tos)
        get() = userCache.getString(TOS, TOS_DEFAULT)

    var isPhoneVerificationEnabled: Boolean
        set(enable) = userCache.putBoolean(PHONE_VERIFICATION_ENABLED, enable)
        get() = userCache.getBoolean(PHONE_VERIFICATION_ENABLED, true)

    var isFirstTimeUser: Boolean
        set(firstTime) = userCache.putBoolean(FIRST_TIME_USER, firstTime)
        get() = userCache.getBoolean(FIRST_TIME_USER, true)

    var authToken: String
        set(token) = userCache.putString(AUTH_TOKEN, token)
        get() = userCache.getString(AUTH_TOKEN, "")

    var restoreHints: List<String>
        set(hints) {
            val hintsString = hints.joinToString(separator = ";")
            userCache.putString(RESTORE_HINTS, hintsString)
        }
        get() {
            val hintsString = userCache.getString(RESTORE_HINTS, "")
            return if (hintsString.isBlank()) listOf() else hintsString.split(';')
        }


    init {
        var userId = userCache.getString(USER_ID_KEY, "")

        if (userId.isEmpty()) {
            userId = UUID.randomUUID().toString()
            userCache.putString(USER_ID_KEY, userId)
        }
        userInfo = UserInfo(userId)
    }

    fun updateUserId(userId: String) {
        userCache.putString(USER_ID_KEY, userId)
        userInfo = UserInfo(userId)
        Log.d("UserRepository", "### user id $userId")
    }


    fun userId(): String {
        Log.d("UserRepository", "### user id ${userInfo.userId}")
        return userInfo.userId
    }
}