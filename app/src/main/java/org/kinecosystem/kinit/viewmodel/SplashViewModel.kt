package org.kinecosystem.kinit.viewmodel

import android.databinding.Observable
import android.databinding.ObservableBoolean
import android.view.View
import org.kinecosystem.kinit.CoreComponentsProvider
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.view.SplashNavigator


class SplashViewModel(private val coreComponents: CoreComponentsProvider,
    var splashNavigator: SplashNavigator?) {

    private companion object {
        const val SPLASH_DURATION: Long = 2000L
        const val CREATE_WALLET_TIMEOUT = 20000L
    }

    val showCreatingWallet: ObservableBoolean = ObservableBoolean(false)
    var callback: Observable.OnPropertyChangedCallback? = null
    private val walletReady = coreComponents.services().walletService.ready

    init {
        coreComponents.scheduler().scheduleOnMain({
            checkReadyToMove()
        }, SPLASH_DURATION)
    }

    fun onResume() {
        coreComponents.analytics().logEvent(Events.Analytics.ViewSplashscreenPage())
    }

    fun onRetryClicked(view: View?) {
        coreComponents.analytics()
            .logEvent(Events.Analytics.ClickRetryButtonOnErrorPage(Analytics.VIEW_ERROR_TYPE_ONBOARDING))
        splashNavigator?.moveToSplashScreen()
        showCreatingWallet.set(true)
        checkReadyToMove()
        coreComponents.services().onBoardingService.appLaunch()
    }

    fun onContactSupportClicked(view: View?) {
        coreComponents.analytics()
            .logEvent(Events.Analytics.ClickContactLinkOnErrorPage(Analytics.VIEW_ERROR_TYPE_ONBOARDING))
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
        if (walletReady.get()) {
            moveToNextScreen()
        } else {
            coreComponents.userRepo().isFirstTimeUser = true
            showCreatingWallet.set(true)
            addWalletReadyCallback()
            scheduleTimeout()
        }
    }

    private fun moveToNextScreen() {
        val userRepo = coreComponents.userRepo()
        if (userRepo.isFirstTimeUser
            && userRepo.isPhoneVerificationEnabled
            && !userRepo.isPhoneVerified) {
            splashNavigator?.moveToTutorialScreen()
        } else if (userRepo.isFirstTimeUser && !userRepo.isPhoneVerificationEnabled) {
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
        coreComponents.scheduler().scheduleOnMain(
            {
                if (walletReady.get()) {
                    moveToNextScreen()
                } else {
                    coreComponents.analytics()
                        .logEvent(Events.Analytics.ViewErrorPage(Analytics.VIEW_ERROR_TYPE_ONBOARDING))
                    splashNavigator?.moveToErrorScreen()
                }
            },
            CREATE_WALLET_TIMEOUT)
    }
}

