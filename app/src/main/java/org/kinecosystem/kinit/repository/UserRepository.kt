package org.kinecosystem.kinit.repository

import android.util.Log
import org.kinecosystem.kinit.model.user.UserInfo
import org.kinecosystem.kinit.server.api.BackupApi
import java.util.*


private const val USER_ID_KEY = "user_id"
private const val IS_REGISTERED_KEY = "is_registered"
private const val IS_FRESH_INSTALL = "is_fresh_install"

private const val FCM_TOKEN_SENT_KEY = "token_sent"
private const val USER_CACHE_NAME = "kin.app.user"
private const val TOS = "tos"
private const val PHONE_VERIFICATION_ENABLED = "PHONE_VERIFICATION_ENABLED"
private const val BACKUP_NAG_ALERT_ENABLED = "BACKUP_NAG_ALERT_ENABLED"

private const val PHONE_VERIFIED = "PHONE_VERIFIED"
private const val FIRST_TIME_USER = "FIRST_TIME_USER"
private const val P2P_MAX_KIN = "P2P_MAX_KIN"
private const val P2P_MIN_KIN = "P2P_MIN_KIN"
private const val P2P_MIN_TASKS = "P2P_MIN_TASKS"
private const val P2P_ENABLED = "P2P_ENABLED"
private const val BACKED_UP = "BACKED_UP"
private const val AUTH_TOKEN = "authToken"
private const val RESTORE_HINTS = "RESTORE_HINTS"
private const val TOS_DEFAULT = "http://www.kinitapp.com/terms-and-privacy-policy"




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
        get() = userCache.getString(TOS, TOS_DEFAULT)

    var isPhoneVerificationEnabled: Boolean
        set(enable) = userCache.putBoolean(PHONE_VERIFICATION_ENABLED, enable)
        get() = userCache.getBoolean(PHONE_VERIFICATION_ENABLED, true)

    var isFirstTimeUser: Boolean
        set(firstTime) = userCache.putBoolean(FIRST_TIME_USER, firstTime)
        get() = userCache.getBoolean(FIRST_TIME_USER, true)

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

    var isBackedup: Boolean
        set(backed) = userCache.putBoolean(BACKED_UP, backed)
        get() = userCache.getBoolean(BACKED_UP, false)

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
            val hintsList = if (hintsString.isBlank()) listOf() else hintsString.split(';')
            return hintsList
        }

    var backUpHints: List<BackupApi.BackUpQuestion> = listOf()

    var isBackupNagAlertEnabled: Boolean
        set(enabled) = userCache.putBoolean(BACKUP_NAG_ALERT_ENABLED, enabled)
        get() = userCache.getBoolean(BACKUP_NAG_ALERT_ENABLED, false)

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