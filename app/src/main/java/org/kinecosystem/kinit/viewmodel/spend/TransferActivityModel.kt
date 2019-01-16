package org.kinecosystem.kinit.viewmodel.spend

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Handler
import android.util.Log
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.spend.EcosystemApp
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.view.transfer.TransferActions
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

const val EXTRA_HAS_ERROR = "EXTRA_HAS_ERROR"
private const val CONNECTION_START_DELAY: Long = 1500


class TransferActivityModel(private val sourceAppName: String, private val app: EcosystemApp, var transferActions: TransferActions?) {

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

    fun startTransferRequestActivity(activity:Activity):Boolean{
        if(app.transferData != null) {
            return transferManager.startTransferRequestActivity(activity, app.identifier, app.transferData.launchActivityFullPath)
        }
        return false
    }

    fun parseResult(context:Context, requestCode:Int, resultCode:Int,  intent:Intent,  onAccountInfoResponse: TransferManager.OnAccountInfoResponse){
        transferManager.parseActivityResult(context, requestCode, resultCode, intent, onAccountInfoResponse)
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