package org.kinecosystem.kinit.view.restore

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.util.SupportUtil
import org.kinecosystem.kinit.view.SingleFragmentActivity
import org.kinecosystem.kinit.view.createWallet.OnboardingCompleteFragment
import org.kinecosystem.kinit.view.support.SupportActivity
import org.kinecosystem.kinit.viewmodel.SupportViewModel
import org.kinecosystem.kinit.viewmodel.restore.RestoreState
import org.kinecosystem.kinit.viewmodel.restore.RestoreWalletActivityMessages
import org.kinecosystem.kinit.viewmodel.restore.RestoreWalletViewModel
import javax.inject.Inject


class RestoreWalletActivity : SingleFragmentActivity(), RestoreWalletActions, RestoreWalletViewModel.RestoreWalletViewModelListener {

    @Inject
    lateinit var userRepository: UserRepository
    private var model: RestoreWalletViewModel = RestoreWalletViewModel()

    override fun onError(msg: RestoreWalletActivityMessages?) {
        showDialog(msg)
    }

    override fun onSuccess() {
        moveToNextScreen()
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, RestoreWalletActivity::class.java)
        private const val REQUEST_CAMERA_PERMISSION = 201
    }

    init {
        model.listener = this
    }

    override fun moveToNextScreen() {
        model.onNext()
        replaceFragment(getFragment(), true)
    }

    override fun moveBack() {
        model.onBack()
        replaceFragment(getFragment(), true, false)
    }

    override fun onBackPressed() {
        if (model.getState() != RestoreState.Welcomeback)
            moveBack()
        else finish()
    }

    override fun beforeSuper() {
        KinitApplication.coreComponent.inject(this)
    }

    override fun getModel(): RestoreWalletViewModel {
        return model
    }

    override fun getFragment(): Fragment {
        return when (model.getState()) {
            RestoreState.Welcomeback -> RestoreWalletWelcomebackFragment.newInstance()
            RestoreState.Intro -> RestoreWalletIntroFragment.newInstance()
            RestoreState.QrScan -> {
                val scannerFragment = RestoreWalletBarcodeScannerFragment.newInstance()
                scannerFragment.listener = object : RestoreWalletBarcodeScannerFragment.BarcodeScannerListener {
                    override fun onQrDecoded(qrCode: String) {
                        scannerFragment.listener = null
                        model.onCodeReceived(qrCode)
                        moveToNextScreen()
                    }
                }
                scannerFragment
            }
            RestoreState.SecurityQuestions -> RestoreWalletAnswerHintsFragment.newInstance()
            RestoreState.Complete -> {
                val onboardingCompleteFragment = OnboardingCompleteFragment.newInstance()
                onboardingCompleteFragment.listener = object : OnboardingCompleteFragment.AfterAnimationListener {
                    override fun onAnimationEnd() {
                        moveToMainScreen()
                    }
                }
                onboardingCompleteFragment
            }
        }
    }

    private fun moveToMainScreen() {
        Navigator(this@RestoreWalletActivity).navigateTo(Navigator.Destination.MAIN_SCREEN)
        finish()
    }

    override fun moveToCreateWallet() {
        Navigator(this@RestoreWalletActivity).navigateTo(Navigator.Destination.WALLET_CREATE)
        finish()
    }

    override fun showDialog(msg: RestoreWalletActivityMessages?) {
        var title: String? = null
        var message: String? = null
        var positiveString = getString(R.string.try_again)
        var negativeString: String? = getString(R.string.contact_support)
        val errorListener = DialogInterface.OnClickListener { dialog, which ->
            if (which == DialogInterface.BUTTON_NEGATIVE) {
                contactSupport()
            }
            dialog.dismiss()
        }

        val createWalletListener = DialogInterface.OnClickListener { dialog, which ->
            if (which == DialogInterface.BUTTON_POSITIVE) {
                dialog.dismiss()
                moveToCreateWallet()
            }
        }
        var listener: DialogInterface.OnClickListener? = errorListener

        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
        when (msg) {
            RestoreWalletActivityMessages.CREATE_WALLET_WARNING -> {
                title = getString(R.string.create_new_wallet_warning_title)
                message = getString(R.string.create_new_wallet_warning_msg)
                positiveString = getString(R.string.create_new_wallet_warning_positive)
                negativeString = getString(R.string.create_new_wallet_warning_negative)
                listener = createWalletListener
            }
            RestoreWalletActivityMessages.NETWORK_ERROR -> {
                title = getString(R.string.network_error_message_title)
                message = getString(R.string.network_error_message_msg)
                positiveString = getString(R.string.network_error_message_positive)
                negativeString = null
            }
            RestoreWalletActivityMessages.QR_ERROR -> {
                // TODO: Better error text
                title = getString(R.string.qr_error_title)
                message = getString(R.string.qr_error_msg)
            }
            RestoreWalletActivityMessages.RESTORE_ERROR -> {
                title = getString(R.string.restore_error_title)
                message = getString(R.string.restore_error_msg)
            }
            RestoreWalletActivityMessages.RESTORE_SERVER_ERROR -> {
                title = getString(R.string.general_problem_title)
                message = getString(R.string.general_problem_body)
            }
        }
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveString, listener)

        if (negativeString != null)
            builder.setNegativeButton(negativeString, listener)
        else
            builder.setCancelable(false)

        builder.show()
    }

    override fun getCameraPermissions() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (model.getState() == RestoreState.Intro) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                moveToNextScreen()
            }
        }
    }

    override fun contactSupport() {
        val urlParams = mapOf("category" to "Backup %26 Restore your Kin","subCategory" to "Other")
        val intent = SupportActivity.getIntent(this, SupportViewModel.Destination.CONTACT_US, urlParams)
        Navigator(this).navigateTo(intent)
    }
}

interface RestoreWalletActions {
    fun moveToNextScreen()
    fun moveBack()
    fun getModel(): RestoreWalletViewModel
    fun moveToCreateWallet()
    fun getCameraPermissions()
    fun contactSupport()
    fun showDialog(msg: RestoreWalletActivityMessages?)
}