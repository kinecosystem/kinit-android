package org.kinecosystem.kinit.view.backup

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.view.SingleFragmentActivity
import org.kinecosystem.kinit.viewmodel.backup.BackupModel

class BackupWalletActivity : SingleFragmentActivity(), BackupActions, UIActions {

    private var model: BackupModel = BackupModel(this)
    private var needToReplace: Boolean = false

    override fun getBackUpModel(): BackupModel {
        return model
    }

    override fun replaceFragment() {
        if (inForeground) {
            needToReplace = false
            replaceFragment(getFragment(), true)
        } else {
            needToReplace = true
        }
    }

    override fun showErrorAlert(alertErrorType: AlertErrorType) {
        //TODO
    }

    override fun onBack() {
        model.onBack()
        replaceFragment(getFragment(), true, false)
    }

    override fun init() {
        model.titles = resources.getStringArray(R.array.questions_count)
        model.initHints()
    }

    override fun onResume() {
        super.onResume()
        if (needToReplace) {
            replaceFragment(getFragment(), true)
            needToReplace = false
        }
    }

    override fun getFragment(): Fragment {
        return when (model.getState()) {
            BackupModel.BackupState.Question -> BackupQuestionAnswerFragment.newInstance()
            BackupModel.BackupState.Summery -> BackupSummeryFragment.newInstance()
            BackupModel.BackupState.QRCode -> BackupQRCodeFragment.newInstance()
            BackupModel.BackupState.Confirm -> BackupConfirmFragment.newInstance()
            BackupModel.BackupState.Complete -> BackupCompleteFragment.newInstance()
            else -> WelcomeBackupFragment.newInstance()
        }
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, BackupWalletActivity::class.java)
    }
}

interface BackupActions {
    fun onBack()
    fun getBackUpModel(): BackupModel
}

interface UIActions {
    fun replaceFragment()
    fun showErrorAlert(alertErrorType: AlertErrorType)
}

enum class AlertErrorType {
    Server, SDK
}