package org.kinecosystem.kinit.viewmodel.spend

import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.model.spend.EcoApplication
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.repository.EcoApplicationsRepository
import javax.inject.Inject

class EcoAppsCategoryViewModel(private val navigator: Navigator, private val categoryId: Int) {

    @Inject
    lateinit var appsRepository: EcoApplicationsRepository

    @Inject
    lateinit var analytics: Analytics
    init {
        KinitApplication.coreComponent.inject(this)
    }

    fun apps(): List<EcoApplication> {
        return appsRepository.apps(categoryId)
    }

    fun categoryTitle() = appsRepository.categoryTitle(categoryId)


    fun onItemClicked(app: EcoApplication, position: Int) {
        navigator.navigateTo(app)
        //TODO
        //analytics.logEvent(Events.Analytics.ClickOfferItemOnSpendPage(offer.provider?.name, offer.price)
    }

}

