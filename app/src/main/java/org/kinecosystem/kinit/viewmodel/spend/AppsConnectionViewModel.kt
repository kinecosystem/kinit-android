package org.kinecosystem.kinit.viewmodel.spend

import android.databinding.ObservableBoolean
import android.os.Handler
import android.util.Log
import org.kinecosystem.kinit.model.spend.EcoApplication
import org.kinecosystem.kinit.view.transfer.TransferActions

const val CONNECTION_START_DELAY: Long = 1500

class AppsConnectionViewModel(app: EcoApplication, private val transferActions: TransferActions?) {

    val appIconUrl: String = app.data.iconUrl
    val onConnected = ObservableBoolean(false)
    private var isPaused = false
    private var delayPassed = false

    init {
        Handler().postDelayed({
            delayPassed = true
            if(!isPaused) {
                transferActions?.onStartConnect()
            }
        }, CONNECTION_START_DELAY)
    }

    fun onResume(){
        isPaused = false
        if(delayPassed){
            transferActions?.onStartConnect()
            delayPassed = false
        }
    }

    fun onPause(){
        isPaused = true
    }


}