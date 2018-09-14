package org.kinecosystem.kinit.viewmodel.info

import android.databinding.ObservableBoolean
import android.view.View
import org.kinecosystem.kinit.BuildConfig
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.util.SupportUtil
import org.kinecosystem.kinit.view.TabViewModel
import javax.inject.Inject

class InfoViewModel(val navigator: Navigator) : TabViewModel {

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var analytics: Analytics

    var showCreateNewBackupAlert = ObservableBoolean(false)

    var isBackedUp = ObservableBoolean(false)

    init {
        KinitApplication.coreComponent.inject(this)
        isBackedUp.set(userRepository.isBackedup)
    }

    var version: String = if (BuildConfig.DEBUG) {
        "${BuildConfig.VERSION_NAME} ${BuildConfig.BUILD_TYPE}"
    } else {
        BuildConfig.VERSION_NAME
    }

    fun onShowingCreateNewBackupAlert(){
        showCreateNewBackupAlert.set(false)
    }

    fun onSendFeedbackClicked(view: View) {
        SupportUtil.openEmailFeedback(view.context, userRepository)
    }

    fun onHelpCenterClicked(view: View) {
        navigator.navigateTo(Navigator.Destination.FAQ)
    }

    fun onStartBackupClicked(view: View) {
        if (isBackedUp.get()) {
            showCreateNewBackupAlert.set(true)
        } else {
            navigator.navigateTo(Navigator.Destination.WALLET_BACKUP)
        }
    }

    override fun onScreenVisibleToUser() {
        isBackedUp.set(userRepository.isBackedup)
        analytics.logEvent(Events.Analytics.ViewProfilePage())
    }
}
