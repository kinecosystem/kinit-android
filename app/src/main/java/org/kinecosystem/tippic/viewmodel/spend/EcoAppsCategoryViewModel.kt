package org.kinecosystem.tippic.viewmodel.spend

import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.analytics.Analytics
import org.kinecosystem.tippic.analytics.Events
import org.kinecosystem.tippic.model.spend.EcoApplication
import org.kinecosystem.tippic.model.spend.isKinTransferSupported
import org.kinecosystem.tippic.navigation.Navigator
import org.kinecosystem.tippic.repository.EcoApplicationsRepository
import javax.inject.Inject

class EcoAppsCategoryViewModel(private val navigator: Navigator, private val categoryId: Int) {

    @Inject
    lateinit var appsRepository: EcoApplicationsRepository

    @Inject
    lateinit var analytics: Analytics
    init {
        TippicApplication.coreComponent.inject(this)
    }

    fun apps(): List<EcoApplication> {
        return appsRepository.apps(categoryId)
    }

    fun categoryTitle() = appsRepository.categoryTitle(categoryId)


    fun onItemClicked(app: EcoApplication) {
        navigator.navigateTo(app)
    }

    fun onActionBtnClicked(app: EcoApplication) {
        navigator.navigateToUrl(app.data.appUrl)
        if(app.isKinTransferSupported()){
            analytics.logEvent(Events.Analytics.ClickSendButtonOnAppItem(app.data.categoryTitle, app.identifier, app.name))
        }else{
            analytics.logEvent(Events.Analytics.ClickGetButtonOnAppItem(app.data.categoryTitle, app.identifier, app.name))
        }
    }

}

