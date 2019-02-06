package org.kinecosystem.kinit.viewmodel.spend

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.view.View
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.repository.EcoApplicationsRepository
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.server.NetworkServices
import org.kinecosystem.kinit.server.OperationCompletionCallback
import org.kinecosystem.kinit.server.api.EcoApplicationsApi
import org.kinecosystem.kinit.view.TabViewModel
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
    var ableToSend: ObservableBoolean

    init {
        KinitApplication.coreComponent.inject(this)
        ableToSend = ecoApplicationsRepository.hasAppToSendObservable
        if (!ecoApplicationsRepository.appCategories.isEmpty()) {
            ableToSend = ecoApplicationsRepository.hasAppToSendObservable
            appCategories.set(ecoApplicationsRepository.appCategories)
            hasData.set(true)
        }
        hasNetwork.set(networkServices.isNetworkConnected())
    }

    fun onLearnMoreClicked(view: View) {
        if (ableToSend.get()) {
            navigator.navigateTo(Navigator.Destination.ECO_APPS_LEARN_MORE)
        } else {
            navigator.navigateTo(Navigator.Destination.ECO_APPS_COMING_SOON)
        }
    }

    override fun onScreenVisibleToUser() {
        hasNetwork.set(networkServices.isNetworkConnected())
        hasData.set(networkServices.isNetworkConnected())
        analytics.logEvent(Events.Analytics.ViewExplorePage())
        checkIfNeedToSeeAppAlert()
        if (appCategories.get().isEmpty()) {
            networkServices.ecoApplicationService.retrieveApps(object : OperationCompletionCallback {
                override fun onSuccess() {
                    appCategories.set(ecoApplicationsRepository.appCategories)
                    hasData.set(true)
                    checkIfNeedToSeeAppAlert()
                }

                override fun onError(errorCode: Int) {
                    hasData.set(false)
                }
            })
        }
    }

    private fun checkIfNeedToSeeAppAlert(){
        if (!userRepository.seenAppsAlert && appCategories.get().isNotEmpty()) {
            showAppsAlert.set(true)
        }
    }
     fun onSeenAppAlert(){
         userRepository.seenAppsAlert = true
     }
}

