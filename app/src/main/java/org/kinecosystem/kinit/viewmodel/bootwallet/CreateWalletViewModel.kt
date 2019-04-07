package org.kinecosystem.kinit.viewmodel.bootwallet

import android.databinding.Observable
import android.databinding.ObservableBoolean
import android.view.View
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.server.CategoriesService
import org.kinecosystem.kinit.server.NetworkServices
import org.kinecosystem.kinit.server.TaskService
import org.kinecosystem.kinit.util.Scheduler
import javax.inject.Inject

class CreateWalletViewModel(override var listener: BootWalletEventsListener?) : BootWalletActions {

    @Inject
    lateinit var analytics: Analytics
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var networkServices: NetworkServices
    @Inject
    lateinit var scheduler: Scheduler
    @Inject
    lateinit var taskService: TaskService
    @Inject
    lateinit var categoriesService: CategoriesService

    var callback: Observable.OnPropertyChangedCallback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(p0: Observable?, p1: Int) {
            categoriesService.retrieveCategories()
            taskService.retrieveAllTasks()
            listener?.onWalletBooted()
        }
    }
    private var walletReady: ObservableBoolean
    private companion object {
        const val WAIT_TIMEOUT: Long = 2000L
        const val CREATE_WALLET_TIMEOUT = 20000L
    }

    init {
        KinitApplication.coreComponent.inject(this)
        walletReady = networkServices.walletService.ready
    }

    override fun bootWallet() {
        networkServices.walletService.initKinWallet()
        listener?.onWalletBooting()
        scheduler.scheduleOnMain({
            checkReadyToMove()
        }, WAIT_TIMEOUT)
    }

    override fun onDestroy() {
        walletReady.removeOnPropertyChangedCallback(callback)
    }

    private fun scheduleTimeout() {
        scheduler.scheduleOnMain(
                {
                    if (walletReady.get()) {
                        categoriesService.retrieveCategories()
                        taskService.retrieveAllTasks()
                        listener?.onWalletBooted()
                    } else {
                        listener?.onWalletBootError()
                        onDestroy()
                    }
                },
                CREATE_WALLET_TIMEOUT)
    }

    private fun checkReadyToMove() {
        if (walletReady.get()) {
            categoriesService.retrieveCategories()
            taskService.retrieveAllTasks()
            listener?.onWalletBooted()
        } else {
            walletReady.addOnPropertyChangedCallback(callback)
            scheduleTimeout()
        }
    }

    override var walletAction: BootAction
        get() = BootAction.CREATE
        set(value) {}

    override fun onRetryClicked(view: View?) {
        analytics.logEvent(Events.Analytics.ClickRetryButtonOnErrorPage(Analytics.VIEW_ERROR_TYPE_ONBOARDING))
        bootWallet()
    }

    override fun onContactSupportClicked(view: View?) {
        analytics.logEvent(Events.Analytics.ClickContactLinkOnErrorPage(Analytics.VIEW_ERROR_TYPE_ONBOARDING))
        listener?.contactSupport()
    }
}

interface BootWalletEventsListener {
    fun onWalletBooted()
    fun onWalletBootError()
    fun onWalletBooting()
    fun contactSupport()
}