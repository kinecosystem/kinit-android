package org.kinecosystem.kinit.viewmodel.bootwallet

import android.view.View
import kin.sdk.migration.common.interfaces.IKinClient
import kin.sdk.migration.common.interfaces.IMigrationManagerCallbacks
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.server.CategoriesService
import org.kinecosystem.kinit.server.NetworkServices
import org.kinecosystem.kinit.server.TaskService
import org.kinecosystem.kinit.util.Scheduler
import java.lang.Exception
import javax.inject.Inject

class MigrateWalletViewModel(override var listener: BootWalletEventsListener?) : BootWalletViewModel {

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

    init {
        KinitApplication.coreComponent.inject(this)
    }

    override fun bootWallet() {
        networkServices.walletService.migrateWallet(object: IMigrationManagerCallbacks{
            override fun onReady(kinClient: IKinClient?) {
                analytics.logEvent(Events.Business.MigrationSucceeded())
                categoriesService.retrieveCategories()
                taskService.retrieveAllTasks()
                listener?.onWalletBooted()
            }

            override fun onMigrationStart() {
                analytics.logEvent(Events.Business.MigrationStarted())
                listener?.onWalletBooting()
            }

            override fun onError(e: Exception?) {
                analytics.logEvent(Events.BILog.MigrationFailed(e?.message))
                listener?.onWalletBootError()
            }
        })
    }

    override var walletAction: BootAction
        get() = BootAction.MIGRATE
        set(value) {}


    override fun onRetryClicked(view: View?) {
        analytics.logEvent(Events.Analytics.ClickRetryButtonOnErrorPage(Analytics.VIEW_ERROR_TYPE_ONBOARDING))
        listener?.onWalletBooting()
    }

    override fun onContactSupportClicked(view: View?) {
        analytics.logEvent(Events.Analytics.ClickContactLinkOnErrorPage(Analytics.VIEW_ERROR_TYPE_ONBOARDING))
        listener?.contactSupport()
    }
}