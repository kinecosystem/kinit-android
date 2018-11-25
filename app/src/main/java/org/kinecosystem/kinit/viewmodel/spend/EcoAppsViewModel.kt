package org.kinecosystem.kinit.viewmodel.spend

import android.databinding.ObservableField
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.repository.EcoApplicationsRepository
import org.kinecosystem.kinit.server.api.EcoApplicationsApi
import org.kinecosystem.kinit.view.TabViewModel
import javax.inject.Inject

class EcoAppsViewModel :
        TabViewModel {

    @Inject
    lateinit var ecoApplicationsRepository: EcoApplicationsRepository
    @Inject
    lateinit var analytics: Analytics
    var appCategories: ObservableField<List<EcoApplicationsApi.AppsCategory>> = ObservableField(listOf())


    init {
        KinitApplication.coreComponent.inject(this)
        appCategories.set(ecoApplicationsRepository.appCategories)
    }

    override fun onScreenVisibleToUser() {
        analytics.logEvent(Events.Analytics.ViewExplorePage())
    }
}

