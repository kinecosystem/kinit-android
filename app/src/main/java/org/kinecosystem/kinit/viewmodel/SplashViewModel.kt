package org.kinecosystem.kinit.viewmodel

import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.server.NetworkServices
import org.kinecosystem.kinit.util.Scheduler
import javax.inject.Inject


class SplashViewModel {
    @Inject
    lateinit var scheduler: Scheduler
    @Inject
    lateinit var analytics: Analytics
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var networkServices: NetworkServices

    val TAG: String = SplashViewModel::class.java.simpleName

    private companion object {
        const val SPLASH_DURATION: Long = 2000L
    }

    interface ISplashViewModel {
        fun showBlackListDialog()
        fun moveToNextScreen(nextScreen: Navigator.Destination)
    }

    var listener: ISplashViewModel? = null

    init {
        KinitApplication.coreComponent.inject(this)
    }

    fun onResume() {
        analytics.logEvent(Events.Analytics.ViewSplashscreenPage())
        scheduleWait()
    }

    private fun scheduleWait(){
        scheduler.scheduleOnMain({
            onTimeout()
        }, SPLASH_DURATION)
    }

    private fun onTimeout() {
        if (networkServices.onBoardingService.isInBlackList) {
            listener?.showBlackListDialog()
        } else {
            if (!userRepository.isRegistered) {
                //try to re register again
                networkServices.onBoardingService.appLaunch()
                scheduleWait()
            } else {
                listener?.moveToNextScreen(getNextScreen())
            }
        }
    }

    private fun getNextScreen(): Navigator.Destination {
        return if (userRepository.isPhoneVerificationEnabled && !userRepository.isPhoneVerified) {
            userRepository.isFirstTimeUser = false
            Navigator.Destination.TUTORIAL
        } else if (userRepository.isFirstTimeUser) {
            userRepository.isFirstTimeUser = false
            Navigator.Destination.TUTORIAL
        } else if (!networkServices.walletService.ready.get()) {
            if (!userRepository.restoreHints.isEmpty())
                Navigator.Destination.WALLET_RESTORE
            else
                Navigator.Destination.WALLET_CREATE
        } else {
            Navigator.Destination.MAIN_SCREEN
        }
    }
}

