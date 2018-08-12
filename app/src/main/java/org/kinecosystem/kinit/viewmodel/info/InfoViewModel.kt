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

    fun onContactSupportClicked(view: View) {
        analytics.logEvent(Events.Analytics.ClickSupportButton())
        analytics.logEvent(Events.Business.SupportRequestSent())
        SupportUtil.openEmailSupport(view.context, userRepository)
    }

    fun onStartBackupClicked(view: View){
        navigator.navigateTo(Navigator.Destination.WALLET_BACKUP)
    }

    override fun onScreenVisibleToUser() {
        analytics.logEvent(Events.Analytics.ViewProfilePage())
    }
}
