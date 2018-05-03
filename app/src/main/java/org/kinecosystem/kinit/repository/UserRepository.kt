package org.kinecosystem.kinit.repository

import android.util.Log
import org.kinecosystem.kinit.model.user.UserInfo
import java.util.*


private const val USER_ID_KEY = "user_id"
private const val IS_REGISTERED_KEY = "is_registered"
private const val IS_FRESH_INSTALL = "is_fresh_install"
private const val FCM_TOKEN_KEY = "token"
private const val USER_CACHE_NAME = "kin.app.user"
private const val TOS = "tos"
private const val PHONE_VERIFICATION_ENABLED = "PHONE_VERIFICATION_ENABLED"
private const val PHONE_VERIFIED = "PHONE_VERIFIED"


class UserRepository(dataStoreProvider: DataStoreProvider) {
    private val userCache: DataStore = dataStoreProvider.dataStore(USER_CACHE_NAME)
    var userInfo: UserInfo
        private set

    var notSentFcmToken: String
        set(token) = userCache.putString(FCM_TOKEN_KEY, token)
        get() = userCache.getString(FCM_TOKEN_KEY, "")

    var isRegistered: Boolean
        set(registered) = userCache.putBoolean(IS_REGISTERED_KEY, registered)
        get() = userCache.getBoolean(IS_REGISTERED_KEY, false)

    var isPhoneVerified: Boolean
        set(phoneVerified) = userCache.putBoolean(PHONE_VERIFIED, phoneVerified)
        get() = userCache.getBoolean(PHONE_VERIFIED, false)

    var isFreshInstall: Boolean
        set(freshInstall) = userCache.putBoolean(IS_FRESH_INSTALL, freshInstall)
        get() = userCache.getBoolean(IS_FRESH_INSTALL, true)

    var tos: String
        set(tos) = userCache.putString(TOS, tos)
        get() = userCache.getString(TOS, "")

    var isPhoneVerificationEnabled: Boolean
        set(enable) = userCache.putBoolean(PHONE_VERIFICATION_ENABLED, enable)
        get() = userCache.getBoolean(PHONE_VERIFICATION_ENABLED, false)

    init {
        var userId = userCache.getString(USER_ID_KEY, "")

        if (userId.isEmpty()) {
            userId = UUID.randomUUID().toString()
            Log.d("UserRepository", "### user id $userId")
            userCache.putString(USER_ID_KEY, userId)
        }
        userInfo = UserInfo(userId)
    }


    fun userId(): String {
        return userInfo.userId
    }
}