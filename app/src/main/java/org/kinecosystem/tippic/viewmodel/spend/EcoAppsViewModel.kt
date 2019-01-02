package org.kinecosystem.tippic.viewmodel.spend

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.view.View
import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.analytics.Analytics
import org.kinecosystem.tippic.analytics.Events
import org.kinecosystem.tippic.navigation.Navigator
import org.kinecosystem.tippic.repository.EcoApplicationsRepository
import org.kinecosystem.tippic.repository.UserRepository
import org.kinecosystem.tippic.server.NetworkServices
import org.kinecosystem.tippic.server.OperationCompletionCallback
import org.kinecosystem.tippic.server.api.EcoApplicationsApi
import org.kinecosystem.tippic.view.TabViewModel
import javax.inject.Inject

class EcoAppsViewModel(val navigator: Navigator) :
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
        TippicApplication.coreComponent.inject(this)
        if (!ecoApplicationsRepository.appCategories.isEmpty()) {
            appCategories.set(ecoApplicationsRepository.appCategories)
            hasData.set(true)
        }
        hasNetwork.set(networkServices.isNetworkConnected())
    }

    fun onComingSoonClicked(view: View) {
        navigator.navigateTo(Navigator.Destination.ECO_APPS_COMING_SOON)
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

