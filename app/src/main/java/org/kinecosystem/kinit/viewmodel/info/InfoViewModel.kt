package org.kinecosystem.kinit.viewmodel.info

import android.content.Intent
import android.databinding.ObservableBoolean
import android.net.Uri
import android.view.View
import org.kinecosystem.kinit.BuildConfig
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.util.SupportUtil
import org.kinecosystem.kinit.view.TabViewModel
import org.kinecosystem.kinit.view.customView.AlertManager
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

    var version: String = "Version " + if (BuildConfig.DEBUG) {
        "${BuildConfig.VERSION_NAME} ${BuildConfig.BUILD_TYPE}"
    } else {
        BuildConfig.VERSION_NAME
    }

    fun openUpdate(view: View?) {
        view?.context?.let { context ->
            val appPackageName = context.packageName
            try {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
            } catch (e: android.content.ActivityNotFoundException) {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
            }
        }
    }

    fun onShowingCreateNewBackupAlert(){
        showCreateNewBackupAlert.set(false)
    }

    fun onSendFeedbackClicked(view: View) {
        AlertManager.showAlert(view.context, R.string.send_feedback_title, R.string.send_feedback_text, R.string.send_feedback_positive, {
            SupportUtil.openEmail(view.context, userRepository, SupportUtil.Type.FEEDBACK)
        }, R.string.i_have_a_problem, {
            navigator.navigateTo(Navigator.Destination.FAQ)
        })
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
