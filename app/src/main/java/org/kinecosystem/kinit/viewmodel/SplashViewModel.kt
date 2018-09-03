package org.kinecosystem.kinit.viewmodel

import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.server.ServicesProvider
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
    lateinit var servicesProvider: ServicesProvider

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
        scheduler.scheduleOnMain({
            if (servicesProvider.onBoardingService.isInBlackList) {
                listener?.showBlackListDialog()
            } else {
                listener?.moveToNextScreen(getNextScreen())
            }
        }, SPLASH_DURATION)
    }

    private fun getNextScreen(): Navigator.Destination {
        return if (userRepository.isPhoneVerificationEnabled && !userRepository.isPhoneVerified) {
            userRepository.isFirstTimeUser = false
            Navigator.Destination.TUTORIAL
        } else if (userRepository.isFirstTimeUser) {
            userRepository.isFirstTimeUser = false
            Navigator.Destination.TUTORIAL
        } else if (!servicesProvider.walletService.ready.get()) {
            Navigator.Destination.WALLET_CREATION
        } else {
            Navigator.Destination.MAIN_SCREEN
        }
    }
}

