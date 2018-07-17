package org.kinecosystem.kinit.repository

import android.util.Log
import org.kinecosystem.kinit.model.user.UserInfo
import java.util.*


private const val USER_ID_KEY = "user_id"
private const val IS_REGISTERED_KEY = "is_registered"
private const val IS_FRESH_INSTALL = "is_fresh_install"
private const val FCM_TOKEN_SENT_KEY = "token_sent"
private const val USER_CACHE_NAME = "kinit.user"
private const val TOS = "tos"
private const val PHONE_VERIFICATION_ENABLED = "PHONE_VERIFICATION_ENABLED"
private const val PHONE_VERIFIED = "PHONE_VERIFIED"
private const val FIRST_TIME_USER = "FIRST_TIME_USER"
private const val P2P_MAX_KIN = "P2P_MAX_KIN"
private const val P2P_MIN_KIN = "P2P_MIN_KIN"
private const val P2P_MIN_TASKS = "P2P_MIN_TASKS"
private const val P2P_ENABLED = "P2P_ENABLED"


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

    var isFirstTimeUser: Boolean
        set(firstTime) = userCache.putBoolean(FIRST_TIME_USER, firstTime)
        get() = userCache.getBoolean(FIRST_TIME_USER, false)

    var p2pMaxKin: Int
        set(max) = userCache.putInt(P2P_MAX_KIN, max)
        get() = userCache.getInt(P2P_MAX_KIN, 0)

    var p2pMinKin: Int
        set(max) = userCache.putInt(P2P_MIN_KIN, max)
        get() = userCache.getInt(P2P_MIN_KIN, 0)

    var p2pMinTasks: Int
        set(max) = userCache.putInt(P2P_MIN_TASKS, max)
        get() = userCache.getInt(P2P_MIN_TASKS, 0)

    var isP2pEnabled: Boolean
        set(enable) = userCache.putBoolean(P2P_ENABLED, enable)
        get() = userCache.getBoolean(P2P_ENABLED, false)

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