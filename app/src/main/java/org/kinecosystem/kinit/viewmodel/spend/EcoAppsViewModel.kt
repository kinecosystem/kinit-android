package org.kinecosystem.kinit.viewmodel.spend

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.repository.EcoApplicationsRepository
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.server.NetworkServices
import org.kinecosystem.kinit.server.OperationCompletionCallback
import org.kinecosystem.kinit.server.api.EcoApplicationsApi
import org.kinecosystem.kinit.view.TabViewModel
import javax.inject.Inject

class EcoAppsViewModel :
        TabViewModel {

    @Inject
    lateinit var ecoApplicationsRepository: EcoApplicationsRepository
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var analytics: Analytics
    @Inject
    lateinit var networkServices: NetworkServices

    var appCategories: ObservableField<List<EcoApplicationsApi.AppsCategory>> = ObservableField(listOf())
    val hasNetwork = ObservableBoolean(true)
    val hasData = ObservableBoolean(true)
    var showAppsAlert = ObservableBoolean(false)

    init {
        KinitApplication.coreComponent.inject(this)
        if (!ecoApplicationsRepository.appCategories.isEmpty()) {
            appCategories.set(ecoApplicationsRepository.appCategories)
            hasData.set(true)
        }
        hasNetwork.set(networkServices.isNetworkConnected())
    }

    override fun onScreenVisibleToUser() {
        hasNetwork.set(networkServices.isNetworkConnected())
        hasData.set(networkServices.isNetworkConnected())
        analytics.logEvent(Events.Analytics.ViewExplorePage())
        if (!userRepository.seenExploreAppsAlert) {
            showAppsAlert.set(true)
            userRepository.seenExploreAppsAlert = true
        }
        if (appCategories.get().isEmpty()) {
            networkServices.ecoApplicationService.retrieveApps(object : OperationCompletionCallback {
                override fun onSuccess() {
                    appCategories.set(ecoApplicationsRepository.appCategories)
                    hasData.set(true)
                }

                override fun onError(errorCode: Int) {
                    hasData.set(false)
                }
            })
        }
    }
}

