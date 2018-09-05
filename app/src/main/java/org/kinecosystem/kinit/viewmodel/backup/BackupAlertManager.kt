package org.kinecosystem.kinit.viewmodel.backup

import android.content.Context
import android.support.annotation.StringRes
import android.text.format.DateUtils
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.repository.DataStore
import org.kinecosystem.kinit.repository.DataStoreProvider
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.view.customView.AlertManager
import javax.inject.Inject

const val DATA_STORE_NAG_ALARM_SEEN = "DATA_STORE_NAG_ALARM_SEEN"
const val LAST_SEEN_NAG_ALARM = "LAST_SEEN_NAG_ALARM"


class BackupAlertManager(val context: Context) {
    enum class BackupAlertType {
        None, BackupNagImmediate, BackupNagWeek, BackupNag2Weeks, BackupNagMonth
    }

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var dataStoreProvider: DataStoreProvider

    @Inject
    lateinit var analytics: Analytics

    private var dataStore: DataStore
    private var navigator: Navigator = Navigator(context)

    init {
        KinitApplication.coreComponent.inject(this)
        dataStore = dataStoreProvider.dataStore(DATA_STORE_NAG_ALARM_SEEN)
    }

    fun showNagAlertIfNeeded() {
        if (userRepository.isBackupNagAlertEnabled && !userRepository.isBackedup) {
            val type = getAlertType()
            if (type != BackupAlertType.None) {
                show(type)
            }
        }
    }

    private fun getAlertType(): BackupAlertType {
        val type = getTypeForCurrentTime()
        if (!seen(type)) {
            return type
        }
        return BackupAlertType.None
    }

    private fun getTypeForCurrentTime(): BackupAlertManager.BackupAlertType {
        val lastSeen = dataStore.getLong(LAST_SEEN_NAG_ALARM, System.currentTimeMillis())
        val timeDiff = System.currentTimeMillis() - lastSeen
        return when (timeDiff) {
            in DateUtils.WEEK_IN_MILLIS * 2..DateUtils.WEEK_IN_MILLIS * 4 -> {//in 2 weeks less the month
                BackupAlertType.BackupNag2Weeks
            }
            in DateUtils.WEEK_IN_MILLIS..DateUtils.WEEK_IN_MILLIS * 2 -> {//in a week less then 2 weeks
                BackupAlertType.BackupNagWeek
            }
            in 0..DateUtils.WEEK_IN_MILLIS -> {//now or less then a week
                BackupAlertType.BackupNagImmediate
            }
            else -> {//more then a month
                BackupAlertType.BackupNagMonth
            }
        }
    }

    private fun seen(alertType: BackupAlertType): Boolean {
        return dataStore.getBoolean(alertType.name, false)
    }

    private fun show(alertType: BackupAlertType) {
        if (alertType == BackupAlertType.None) return
        when (alertType) {
            BackupAlertType.BackupNagImmediate -> {
                showPositiveBackupNag(R.string.back_up_your_kin, R.string.backup_message_phone_lost, "Day 1")
            }
            BackupAlertType.BackupNagWeek -> {
                showPositiveBackupNag(R.string.your_kin_is_really_rolling_in, R.string.backup_message_safe,"Day 7")
            }
            BackupAlertType.BackupNag2Weeks -> {
                showPositiveBackupNag(R.string.we_care_about_your_kin, R.string.backup_message_easy, "Day 14")
            }
            BackupAlertType.BackupNagMonth -> {
                showPositiveBackupNag(R.string.knock_knock, R.string.backup_message_today,"Day 30")
            }
        }
        dataStore.putBoolean(alertType.name, true)
        dataStore.putLong(LAST_SEEN_NAG_ALARM, System.currentTimeMillis())
    }

    private fun showPositiveBackupNag(@StringRes title: Int, @StringRes message: Int, eventParam:String) {
        AlertManager.showPositiveAlert(context, title, message, R.string.back_up, {
            navigator.navigateTo(Navigator.Destination.WALLET_BACKUP)
            analytics.logEvent(Events.Analytics.ClickBackupButtonOnBackupNotificationPopup(eventParam))
        }, R.string.cancel, {})
        analytics.logEvent(Events.Analytics.ViewBackupNotificationPopup(eventParam))
    }
}