package org.kinecosystem.kinit.viewmodel

import android.databinding.Observable
import android.databinding.ObservableBoolean
import android.view.View
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.server.ServicesProvider
import org.kinecosystem.kinit.server.TaskService
import org.kinecosystem.kinit.util.Scheduler
import javax.inject.Inject

class CreateWalletViewModel {

    @Inject
    lateinit var analytics: Analytics
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var servicesProvider: ServicesProvider
    @Inject
    lateinit var scheduler: Scheduler
    @Inject
    lateinit var taskService: TaskService


    var callback: Observable.OnPropertyChangedCallback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(p0: Observable?, p1: Int) {
            taskService.retrieveNextTask()
            listener?.onWalletCreated()
        }
    }
    private var walletReady: ObservableBoolean
    var listener: CreateWalletEventsListener? = null

    private companion object {
        const val WAIT_TIMEOUT: Long = 2000L
        const val CREATE_WALLET_TIMEOUT = 20000L
    }

    init {
        KinitApplication.coreComponent.inject(this)
        walletReady = servicesProvider.walletService.ready
    }

    fun initWallet() {
        servicesProvider.walletService.initKinWallet()
        listener?.onWalletCreating()
        scheduler.scheduleOnMain({
            checkReadyToMove()
        }, WAIT_TIMEOUT)
    }

    fun onDestroy() {
        walletReady.removeOnPropertyChangedCallback(callback)
    }

    private fun scheduleTimeout() {
        scheduler.scheduleOnMain(
                {
                    if (walletReady.get()) {
                        taskService.retrieveNextTask()
                        listener?.onWalletCreated()
                    } else {
                        listener?.onCreateWalletError()
                        onDestroy()
                    }
                },
                CREATE_WALLET_TIMEOUT)
    }

    private fun checkReadyToMove() {
        if (walletReady.get()) {
            taskService.retrieveNextTask()
            listener?.onWalletCreated()
        } else {
            walletReady.addOnPropertyChangedCallback(callback)
            scheduleTimeout()
        }
    }

    fun onRetryClicked(view: View?) {
        analytics.logEvent(Events.Analytics.ClickRetryButtonOnErrorPage(Analytics.VIEW_ERROR_TYPE_ONBOARDING))
        initWallet()
        checkReadyToMove()
    }

    fun onContactSupportClicked(view: View?) {
        analytics.logEvent(Events.Analytics.ClickContactLinkOnErrorPage(Analytics.VIEW_ERROR_TYPE_ONBOARDING))
        listener?.contactSupport(userRepository)
    }
}

interface CreateWalletEventsListener {
    fun onWalletCreated()
    fun onCreateWalletError()
    fun onWalletCreating()
    fun contactSupport(userRepository: UserRepository)
}