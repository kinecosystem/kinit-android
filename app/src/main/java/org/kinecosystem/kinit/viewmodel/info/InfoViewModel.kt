package org.kinecosystem.kinit.viewmodel.info

import android.view.View
import org.kinecosystem.kinit.BuildConfig
import org.kinecosystem.kinit.CoreComponentsProvider
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.util.SupportUtil
import org.kinecosystem.kinit.view.TabViewModel

class InfoViewModel(private val coreComponents: CoreComponentsProvider) : TabViewModel {

    var version: String = if (BuildConfig.DEBUG) {
        "${BuildConfig.VERSION_NAME} ${BuildConfig.BUILD_TYPE}"
    } else {
        BuildConfig.VERSION_NAME
    }

    fun onContactSupportClicked(view: View) {
        coreComponents.analytics().logEvent(Events.Analytics.ClickSupportButton())
        coreComponents.analytics().logEvent(Events.Business.SupportRequestSent())
        SupportUtil.openEmailSupport(view.context, coreComponents.userRepo())
    }

    override fun onScreenVisibleToUser() {
        coreComponents.analytics().logEvent(Events.Analytics.ViewProfilePage())
    }
}
