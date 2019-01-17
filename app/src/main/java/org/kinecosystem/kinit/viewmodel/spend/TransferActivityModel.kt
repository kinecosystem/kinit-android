package org.kinecosystem.kinit.viewmodel.spend

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
    val REQUEST_CODE = 77
    val EXTRA_SOURCE_APP_NAME = "EXTRA_SOURCE_APP_NAME"
    private var delayPassed: Boolean = false
    private var isPaused: Boolean = false
    private var isConnectionStarted: Boolean = false

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

    fun createTransferIntent(context: Context): Intent? {
        val intent = Intent()
        intent.`package` = app.identifier
        intent.component = ComponentName(app.identifier, app.transferData?.launchActivityFullPath)
        intent.putExtra(EXTRA_SOURCE_APP_NAME, sourceAppName)
        val queryIntentServices: MutableList<ResolveInfo> = context.packageManager.queryIntentActivities(intent, 0)
        return if (!queryIntentServices.isEmpty()) {
            intent
        } else {
            null
        }
    }

    fun parseCancel(intent: Intent?) {
        intent?.let {
            if (it.hasExtra(EXTRA_HAS_ERROR) && it.getBooleanExtra(EXTRA_HAS_ERROR, false)) {
                transferActions?.onConnectionError()
                analytics.logEvent(Events.Business.CrossAppKinFailure("", Analytics.FAILURE_TYPE_ERROR))
            } else {
                analytics.logEvent(Events.Business.CrossAppKinFailure("", Analytics.FAILURE_TYPE_CANCEL))
                transferActions?.onClose()
            }
        } ?: run {
            analytics.logEvent(Events.Business.CrossAppKinFailure("", Analytics.FAILURE_TYPE_CANCEL))
            transferActions?.onClose()
        }
    }

    fun parseData(context: Context, intent: Intent?) {
        if (intent != null && intent.data != null) {
            try {
                val uri = intent.data
                val inputStream = context.contentResolver.openInputStream(uri)
                val reader = BufferedReader(InputStreamReader(inputStream!!))
                val stringBuilder = StringBuilder()

                var data: String? = reader.readLine()
                while (data != null) {
                    stringBuilder.append(data).append('\n')
                    data = reader.readLine()
                }
                val address = stringBuilder.toString()
                if (address.isNullOrEmpty()) {
                    transferActions?.onConnectionError()
                } else {
                    userRepository.updateApplicationAddress(app.identifier, address)
                    transferActions?.onConnected()
                }
            } catch (e: Exception) {
                transferActions?.onConnectionError()
            }
        }

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