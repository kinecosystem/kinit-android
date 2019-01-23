package org.kinecosystem.kinit.viewmodel.spend

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.os.Handler
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Analytics.FAILURE_TYPE_ERROR
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.spend.EcosystemApp
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.server.NetworkServices
import org.kinecosystem.kinit.server.OperationResultCallback
import org.kinecosystem.kinit.view.transfer.TransferActions
import javax.inject.Inject

const val TRANSFER_TIMEOUT: Long = 30000

class TransferringToAppViewModel(val navigator: Navigator, val app: EcosystemApp, val amount: Int, var transferActions: TransferActions?) {

    @Inject
    lateinit var servicesProvider: NetworkServices
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var analytics: Analytics

    var address: String?
    var balance: ObservableField<String>
    var appIconUrl = app.data.iconUrl
    var onTransactionComplete: ObservableBoolean = ObservableBoolean(false)
    var delayFadeoutAnim = ObservableInt(5000)
    var delayFadeInAnim = ObservableInt(5300)
    private var isPaused = false
    private var reachedTimeout = false
    private var onTransactionFailed = false

    init {
        KinitApplication.coreComponent.inject(this)
        address = userRepository.getApplicationAddress(app.identifier)
        balance = servicesProvider.walletService.balance
        transfer()
        val handler = Handler()
        handler.postDelayed({
            if (!onTransactionComplete.get() && !onTransactionFailed) {
                reachedTimeout = true
                if (!isPaused) {
                    onTimeout()
                }
            }
        }, TRANSFER_TIMEOUT)
    }

    private fun onTimeout() {
        transferActions?.onTransferTimeout()
    }

    fun onFinish() {
        transferActions?.onClose()
    }

    fun transfer() {
        address?.let {
            servicesProvider.offerService.app2appTransfer(it, app.sid, amount, object : OperationResultCallback<String> {
                override fun onResult(result: String) {
                    onTransactionComplete.set(true)
                    transferActions?.onTransferComplete()
                    analytics.logEvent(Events.Business.CrossAppKinSent(app.data.categoryTitle, app.identifier, app.name, amount))
                }

                override fun onError(errorCode: Int) {
                    onTransactionFailed = true
                    transferActions?.onTransferFailed()
                    analytics.logEvent(Events.Business.CrossAppKinFailure("", FAILURE_TYPE_ERROR))
                }
            })
        }
    }

    fun onResume() {
        isPaused = false
        if (reachedTimeout) {
            onTimeout()
        }
    }

    fun onPause() {
        isPaused = true
    }

    fun onDestroy() {
        transferActions = null
    }
}
