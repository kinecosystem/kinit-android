package org.kinecosystem.kinit.viewmodel.spend

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.model.spend.EcosystemApp
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.view.transfer.TransferActions
import org.kinecosystem.kinit.viewmodel.spend.TransferManager.AccountInfoResponseListener
import javax.inject.Inject

private const val CONNECTION_START_DELAY: Long = 1500


class TransferActivityModel(private val app: EcosystemApp, var transferActions: TransferActions?) {
    private var delayPassed: Boolean = false
    private var isPaused: Boolean = false
    private var isConnectionStarted: Boolean = false
    private val transferManager = TransferManager()

    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var analytics: Analytics

    init {
        KinitApplication.coreComponent.inject(this)
    }

    fun startConnectWithDelay() {
        Handler().postDelayed({
            delayPassed = true
            startConnection()
        }, CONNECTION_START_DELAY)
    }

    fun startTransferRequestActivity(activity: Activity) {
        if (app.transferData != null) {
            val started = transferManager.startTransferRequestActivity(activity, app.identifier, app.transferData.launchActivityFullPath)
            if (!started) {
                transferActions?.onConnectionError()
            }
        } else {
            transferActions?.onConnectionError()
        }
    }

    fun parseResult(context: Context, requestCode: Int, resultCode: Int, intent: Intent) {
        transferManager.parseActivityResult(context, requestCode, resultCode, intent, object : AccountInfoResponseListener {
            override fun onCancel() {
                transferActions?.onClose()
            }

            override fun onError(error: String?) {
                transferActions?.onConnectionError()
            }

            override fun onAddressReceived(address: String?) {
                address?.let {
                    if (!it.isEmpty()) {
                        userRepository.updateApplicationAddress(app.identifier, it)
                        transferActions?.onConnected()
                    } else {
                        transferActions?.onConnectionError()
                    }
                } ?: run {
                    transferActions?.onConnectionError()
                }
            }
        })
    }

    fun onResume() {
        isPaused = false
        if (delayPassed && !isConnectionStarted) {
            startConnection()
        }
    }

    private fun startConnection() {
        if (!isPaused && !isConnectionStarted) {
            isConnectionStarted = true
            transferActions?.onStartConnect()
        }
    }

    fun onPause() {
        isPaused = true
    }

    fun onDestroy() {
        transferActions = null
    }

}