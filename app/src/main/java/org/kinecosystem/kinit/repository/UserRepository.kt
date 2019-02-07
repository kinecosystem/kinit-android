package org.kinecosystem.kinit.repository

import android.databinding.ObservableBoolean
import android.util.Log
import org.kinecosystem.kinit.model.user.UserInfo
import org.kinecosystem.kinit.server.api.BackupApi
import java.util.*
import kotlin.collections.HashMap


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
private const val FAQ_URL = "FAQ_URL"
private const val FEEDBACK_URL = "FEEDBACK_URL"
private const val CONTACT_US_URL = "CONTACT_US_URL"
private const val P2P_MIN_TASKS = "P2P_MIN_TASKS"
private const val P2P_ENABLED = "P2P_ENABLED"
private const val BACKED_UP = "BACKED_UP"
private const val AUTH_TOKEN = "authToken"
private const val RESTORE_HINTS = "RESTORE_HINTS"
private const val TOS_DEFAULT = "http://www.kinitapp.com/terms-and-privacy-policy"
private const val CONTACT_US_DEFAULT = "https://s3.amazonaws.com/kinapp-static/faq2/support/contact-us.html"
private const val FAQ_DEFAULT = "https://s3.amazonaws.com/kinapp-static/faq2/index.html"
private const val FEEDBACK_DEFAULT = "https://s3.amazonaws.com/kinapp-static/faq2/support/feedback.html"
private const val FORCE_UPDATE = "FORCE_UPDATE"
private const val IS_UPDATE_AVAILABLE = "IS_UPDATE_AVAILABLE"
private const val SEEN_NEW_SPEND_POLICY = "SEEN_NEW_SPEND_POLICY"
private const val SEEN_APPS_ALERT = "SEEN_APPS_ALERT"




class UserRepository(dataStoreProvider: DataStoreProvider) {
    private val userCache: DataStore = dataStoreProvider.dataStore(USER_CACHE_NAME)
    var userInfo: UserInfo
        private set

    private var appsPublicAddresses = mutableMapOf<String,String>()

    var fcmTokenSent: Boolean
        set(tokenSent) = userCache.putBoolean(FCM_TOKEN_SENT_KEY, tokenSent)
        get() = userCache.getBoolean(FCM_TOKEN_SENT_KEY, false)

    var isRegistered: Boolean
        set(registered) = userCache.putBoolean(IS_REGISTERED_KEY, registered)
        get() = userCache.getBoolean(IS_REGISTERED_KEY, false)

    var faqUrl: String
        set(url) = userCache.putString(FAQ_URL, url)
        get() = userCache.getString(FAQ_URL, FAQ_DEFAULT)

    var feedbackUrl: String
        set(url) = userCache.putString(FEEDBACK_URL, url)
        get() = userCache.getString(FEEDBACK_URL, FEEDBACK_DEFAULT)

    var contactUsUrl: String
        set(url) = userCache.putString(CONTACT_US_URL, url)
        get() = userCache.getString(CONTACT_US_URL, CONTACT_US_DEFAULT)

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
            return if (hintsString.isBlank()) listOf() else hintsString.split(';')
        }

    var backUpHints: List<BackupApi.BackUpQuestion> = listOf()

    var isBackupNagAlertEnabled: Boolean
        set(enabled) = userCache.putBoolean(BACKUP_NAG_ALERT_ENABLED, enabled)
        get() = userCache.getBoolean(BACKUP_NAG_ALERT_ENABLED, false)

    var seenNewSPendPolicy: Boolean
        set(seen) = userCache.putBoolean(SEEN_NEW_SPEND_POLICY, seen)
        get() = userCache.getBoolean(SEEN_NEW_SPEND_POLICY, false)

    var seenAppsAlert: Boolean
        set(seen) = userCache.putBoolean(SEEN_APPS_ALERT, seen)
        get() = userCache.getBoolean(SEEN_APPS_ALERT, false)

    init {
        var userId = userCache.getString(USER_ID_KEY, "")

        if (userId.isEmpty()) {
            userId = UUID.randomUUID().toString()
            userCache.putString(USER_ID_KEY, userId)
        }
        userInfo = UserInfo(userId)
    }


    fun updateApplicationAddress(app:String, address:String){
        appsPublicAddresses[app] = address
    }

    fun getApplicationAddress(app:String)  = appsPublicAddresses[app]

    fun updateUserId(userId: String) {
        userCache.putString(USER_ID_KEY, userId)
        userInfo = UserInfo(userId)
        Log.d("UserRepository", "user id $userId")
    }


    fun userId(): String {
        Log.d("UserRepository", "user id ${userInfo.userId}")
        return userInfo.userId
    }
}