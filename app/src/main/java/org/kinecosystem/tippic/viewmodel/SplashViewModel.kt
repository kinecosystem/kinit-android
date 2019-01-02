package org.kinecosystem.tippic.viewmodel

import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.analytics.Analytics
import org.kinecosystem.tippic.analytics.Events
import org.kinecosystem.tippic.navigation.Navigator
import org.kinecosystem.tippic.repository.UserRepository
import org.kinecosystem.tippic.server.NetworkServices
import org.kinecosystem.tippic.util.Scheduler
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
        TippicApplication.coreComponent.inject(this)
    }

    fun onResume() {
        analytics.logEvent(Events.Analytics.ViewSplashscreenPage())
        scheduler.scheduleOnMain({
            if (networkServices.onBoardingService.isInBlackList) {
                listener?.showBlackListDialog()
            } else {
                listener?.moveToNextScreen(getNextScreen())
            }
        }, SPLASH_DURATION)
    }

    private fun getNextScreen(): Navigator.Destination {
        return if (userRepository.isPhoneVerificationEnabled && !userRepository.isPhoneVerified) {
            userRepository.isFirstTimeUser = false
            Navigator.Destination.PHONE_VERIFY
        } else if (!networkServices.walletService.ready.get()) {
            Navigator.Destination.WALLET_CREATE
        } else {
            Navigator.Destination.MAIN_SCREEN
        }
    }
}

