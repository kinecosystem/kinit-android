package org.kinecosystem.kinit.viewmodel

import android.content.Intent
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.os.AsyncTask
import android.support.annotation.IntDef
import android.view.View
import com.kin.ecosystem.transfer.AccountInfoManager
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.repository.UserRepository
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Inject

class AccountInfoViewModel(val accountInfoManager: AccountInfoManager, val intent: Intent?) {

    companion object {
        const val TASK_STATE_UNDEFINED = 0
        const val TASK_STATE_SUCCESS = 10
        const val TASK_STATE_FAILURE = 20
        const val EXTRA_SOURCE_APP_NAME = "EXTRA_SOURCE_APP_NAME"

    }

    @IntDef(TASK_STATE_UNDEFINED, TASK_STATE_SUCCESS, TASK_STATE_FAILURE)
    @Retention(RetentionPolicy.SOURCE)
    internal annotation class TaskState

    @Inject
    lateinit var walletService: UserRepository


    @TaskState
    private var taskState = TASK_STATE_UNDEFINED

    private var asyncTask: AccountInfoAsyncTask
    private var isPaused = false
    val onClose: ObservableBoolean = ObservableBoolean(false)
    val enableBtn: ObservableBoolean = ObservableBoolean(false)
    val callerAppName: ObservableField<String> = ObservableField("")

    init {
        KinitApplication.coreComponent.inject(this)
        onClose.set(false)

        processIntent()
        asyncTask = AccountInfoAsyncTask()
        asyncTask.execute()
    }

    private fun processIntent() {
        intent?.let {
            if (it.hasExtra(EXTRA_SOURCE_APP_NAME)) {
                val sourceApp = it.getStringExtra(EXTRA_SOURCE_APP_NAME)
                if (!sourceApp.isNullOrBlank()) {
                    callerAppName.set(sourceApp)
                } else {
                    onError()
                }
            } else {
                onError()
            }
        } ?: run {
            onError()
        }
    }

    fun onAgreeClicked(view: View?) {
        accountInfoManager.respondOk()
        onClose.set(true)
    }

    fun onCloseClicked(view: View?) {
        accountInfoManager.respondCancel()
        onClose.set(true)

    }

    fun onBackPressed() {
        accountInfoManager.respondCancel()
    }

    fun onDetach() {
        asyncTask.cancel(true)
    }

    fun onResume() {
        isPaused = false
        checkTaskState()
    }

    fun onPause() {
        isPaused = true
    }


    private fun onError() {
        accountInfoManager.respondError()
        onClose.set(true)
    }

    private fun onTaskComplete(state: Int?) {
        state?.let {
            taskState = it
        } ?: kotlin.run {
            taskState = TASK_STATE_FAILURE
        }
        if (!isPaused) {
            checkTaskState()
        }
    }

    private fun checkTaskState() {
        if (taskState == TASK_STATE_SUCCESS) {
            enableBtn.set(true)
            taskState = TASK_STATE_UNDEFINED
        } else if (taskState == TASK_STATE_FAILURE) {
            onError()
            taskState = TASK_STATE_UNDEFINED
        }
    }


    private inner class AccountInfoAsyncTask : AsyncTask<Void, Void, Int>() {

        override fun doInBackground(vararg args: Void): Int? {
            @TaskState var state = TASK_STATE_FAILURE
            val address = getPublicAddress()
            address?.let {
                if (it.isNotBlank()) {
                    if (accountInfoManager.init(address)) {
                        state = TASK_STATE_SUCCESS
                    }
                }
            }
            return state
        }

        private fun getPublicAddress(): String? {
            return walletService.userInfo.publicAddress
        }

        override fun onPostExecute(state: Int?) {
            super.onPostExecute(state)
            onTaskComplete(state)
        }
    }


}