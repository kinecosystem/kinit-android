package org.kinecosystem.kinit.viewmodel.spend

import android.content.Context
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.spend.EcosystemApp
import org.kinecosystem.kinit.model.spend.isKinTransferSupported
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.repository.EcoApplicationsRepository
import org.kinecosystem.kinit.util.GeneralUtils
import javax.inject.Inject

class EcoAppsCategoryViewModel(private val navigator: Navigator, private val categoryId: Int) {

    @Inject
    lateinit var appsRepository: EcoApplicationsRepository

    @Inject
    lateinit var analytics: Analytics

    init {
        KinitApplication.coreComponent.inject(this)
    }

    fun apps(): List<EcosystemApp> {
        return appsRepository.apps(categoryId)
    }

    fun categoryTitle() = appsRepository.categoryTitle(categoryId)


    fun onItemClicked(app: EcosystemApp) {
        navigator.navigateTo(app)
    }

    fun onActionBtnClicked(app: EcosystemApp, context: Context) {
        if (app.isKinTransferSupported() && GeneralUtils.isAppInstalled(context, app.identifier)) {
            navigator.navigateToTransfer(app, false)
            analytics.logEvent(Events.Analytics.ClickSendButtonOnAppItem(app.data.categoryTitle, app.identifier, app.name))
        } else {
            navigator.navigateToUrl(app.data.appUrl)
            analytics.logEvent(Events.Analytics.ClickGetButtonOnAppItem(app.data.categoryTitle, app.identifier, app.name))
        }
    }

}

