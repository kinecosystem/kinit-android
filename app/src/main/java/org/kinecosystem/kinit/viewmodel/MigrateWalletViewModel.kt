package org.kinecosystem.kinit.viewmodel

import android.databinding.Observable
import android.databinding.ObservableBoolean
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

class MigrateWalletViewModel {

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

    var listener: MigrateWalletEventsListener? = null

    init {
        KinitApplication.coreComponent.inject(this)
    }

    fun migrateWallet() {
        networkServices.walletService.migrateWallet(object: IMigrationManagerCallbacks{
            override fun onReady(kinClient: IKinClient?) {
                categoriesService.retrieveCategories()
                taskService.retrieveAllTasks()
                listener?.onWalletMigrated()
            }

            override fun onMigrationStart() {
                listener?.onWalletMigrating()
            }

            override fun onError(e: Exception?) {
                listener?.onMigrateWalletError()
            }
        })
    }


    fun onRetryClicked(view: View?) {
        // TODO: logs
        listener?.onWalletMigrating()
    }

    fun onContactSupportClicked(view: View?) {
        // TODO: logs
        listener?.contactSupport()
    }
}

interface MigrateWalletEventsListener {
    fun onWalletMigrated()
    fun onMigrateWalletError()
    fun onWalletMigrating()
    fun contactSupport()
}