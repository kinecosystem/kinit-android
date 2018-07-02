package org.kinecosystem.kinit.viewmodel

import android.databinding.Observable
import android.databinding.ObservableBoolean
import android.view.View
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.network.ServicesProvider
import org.kinecosystem.kinit.repository.TasksRepository
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.util.Scheduler
import org.kinecosystem.kinit.view.SplashNavigator
import javax.inject.Inject


class SplashViewModel(var splashNavigator: SplashNavigator?) {

    private companion object {
        const val SPLASH_DURATION: Long = 2000L
        const val CREATE_WALLET_TIMEOUT = 20000L
    }

    @Inject
    lateinit var scheduler: Scheduler
    @Inject
    lateinit var analytics: Analytics
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var servicesProvider: ServicesProvider
    @Inject
    lateinit var questionnaireRepository: TasksRepository

    val showCreatingWallet: ObservableBoolean = ObservableBoolean(false)
    var callback: Observable.OnPropertyChangedCallback? = null
    var googlePlayServicesAvailable: Boolean = true
    private var walletReady: ObservableBoolean

    init {
        KinitApplication.coreComponent.inject(this)
        walletReady = servicesProvider.walletService.ready
    }

    fun onResume() {
        scheduler.scheduleOnMain({
            checkReadyToMove()
        }, SPLASH_DURATION)
        analytics.logEvent(Events.Analytics.ViewSplashscreenPage())
    }

    fun onRetryClicked(view: View?) {
        analytics.logEvent(Events.Analytics.ClickRetryButtonOnErrorPage(Analytics.VIEW_ERROR_TYPE_ONBOARDING))
        splashNavigator?.moveToSplashScreen()
        showCreatingWallet.set(true)
        checkReadyToMove()
        servicesProvider.onBoardingService.appLaunch()
    }

    fun onContactSupportClicked(view: View?) {
        analytics.logEvent(Events.Analytics.ClickContactLinkOnErrorPage(Analytics.VIEW_ERROR_TYPE_ONBOARDING))
        splashNavigator?.openContactSupport()
    }

    fun onDestroy() {
        splashNavigator = null
        if (callback != null) {
            walletReady.removeOnPropertyChangedCallback(callback)
            callback = null
        }
    }

    private fun checkReadyToMove() {
        if (googlePlayServicesAvailable) {
            if (walletReady.get()) {
                moveToNextScreen()
            } else {
                userRepository.isFirstTimeUser = true
                showCreatingWallet.set(true)
                addWalletReadyCallback()
                scheduleTimeout()
            }
        }
    }

    private fun moveToNextScreen() {
        if (userRepository.isFirstTimeUser
            && userRepository.isPhoneVerificationEnabled
            && !userRepository.isPhoneVerified) {
            splashNavigator?.moveToTutorialScreen()
        } else if (userRepository.isFirstTimeUser && !userRepository.isPhoneVerificationEnabled) {
            splashNavigator?.moveToTutorialScreen()
        } else {
            splashNavigator?.moveToMainScreen()
        }
    }


    private fun addWalletReadyCallback() {
        if (callback == null) {
            callback = object : Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(p0: Observable?, p1: Int) {
                    splashNavigator?.moveToTutorialScreen()
                }
            }
            walletReady.addOnPropertyChangedCallback(callback)
        }
    }

    private fun scheduleTimeout() {
        scheduler.scheduleOnMain(
            {
                if (walletReady.get()) {
                    moveToNextScreen()
                } else {
                    analytics.logEvent(Events.Analytics.ViewErrorPage(Analytics.VIEW_ERROR_TYPE_ONBOARDING))
                    splashNavigator?.moveToErrorScreen()
                }
            },
            CREATE_WALLET_TIMEOUT)
    }
}

