package org.kinecosystem.kinit.view.backup

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.view.SingleFragmentActivity
import org.kinecosystem.kinit.viewmodel.Backup.BackUpModel

class BackupWelcomeActivity : SingleFragmentActivity(), BackUpActions {

    private var model: BackUpModel = BackUpModel()

    override fun getBackUpModel(): BackUpModel {
        return model
    }

    override fun onNext() {
        model.onNext()
        replaceFragment(getFragment(), true)
    }

    override fun onBack() {
        model.onBack()
        replaceFragment(getFragment(), true, false)
    }

    override fun init() {
        model.titles = resources.getStringArray(R.array.questions_count)
        model.initHints()
    }

    override fun getFragment(): Fragment {
        return when (model.getState()) {
            BackUpModel.BackupState.Question -> BackupQuestionAnswerFragment.newInstance()
            BackUpModel.BackupState.Summery -> BackupSummeryFragment.newInstance()
            BackUpModel.BackupState.QRCode -> BackupQRCodeFragment.newInstance()
            BackUpModel.BackupState.Confirm -> BackupConfirmFragment.newInstance()
            BackUpModel.BackupState.Complete -> BackupCompleteFragment.newInstance()
            else -> WelcomeBackupFragment.newInstance()
        }
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, BackupWelcomeActivity::class.java)
    }
}

interface BackUpActions {
    fun onNext()
    fun onBack()
    fun getBackUpModel(): BackUpModel
}