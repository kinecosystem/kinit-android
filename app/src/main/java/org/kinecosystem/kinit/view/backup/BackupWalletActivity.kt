package org.kinecosystem.kinit.view.backup

import android.content.Context
import android.content.Intent
import android.databinding.Observable
import android.databinding.ObservableBoolean
import android.support.v4.app.Fragment
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.view.SingleFragmentActivity
import org.kinecosystem.kinit.viewmodel.backup.BackupModel

class BackupWalletActivity : SingleFragmentActivity(), BackupActions {

    private var model: BackupModel = BackupModel()
    private var needToWait: Boolean = false
    private var listener : Observable.OnPropertyChangedCallback ? = null

    override fun getBackUpModel(): BackupModel {
        return model
    }

    override fun onNext() {
        model.onNext()
        needToWait = model.needToWaitForResponse
        if (!needToWait) {
            replaceFragment(getFragment(), true)
        }else{
            addListener()
        }

    }

    override fun onBack() {
        model.onBack()
        replaceFragment(getFragment(), true, false)
    }

    override fun init() {
        model.titles = resources.getStringArray(R.array.questions_count)
        model.initHints()
    }

    override fun onPause() {
        super.onPause()
        removeListener()
    }

    override fun onResume() {
        super.onResume()
        if (needToWait) {
            if (model.responseReadyToNextStep.get()) {
                replaceFragment(getFragment(), true)
            } else {
                addListener()
            }
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

    private fun addListener(){
        if ( listener ==  null) {
            listener = object : Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                    if (sender is ObservableBoolean && sender.get()) {
                        replaceFragment(getFragment(), true)
                    }
                }

            }
            model.responseReadyToNextStep.addOnPropertyChangedCallback(listener)
        }
    }

    private fun removeListener(){
        if (listener != null ){
            model.responseReadyToNextStep.removeOnPropertyChangedCallback(listener)
        }
    }
}

interface BackupActions {
    fun onNext()
    fun onBack()
    fun getBackUpModel(): BackupModel
}