package org.kinecosystem.kinit.view.transfer

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.model.spend.EcoApplication
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.view.BaseActivity
import org.kinecosystem.kinit.view.customView.AlertManager
import org.kinecosystem.kinit.viewmodel.spend.TransferActivityModel

private const val APP_PARAM = "TransferActivity_APP_PARAM"
private const val FROM_APP_DETAIL_PARAM = "FROM_APP_DETAIL_PARAM"

class TransferActivity : BaseActivity(), TransferActions {

    lateinit var navigator: Navigator
    lateinit var model: TransferActivityModel
    lateinit var app: EcoApplication
    private var returnToAppDetail: Boolean = false
    private var isFinished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        KinitApplication.coreComponent.inject(this)
        super.onCreate(savedInstanceState)
        navigator = Navigator(this)
        setContentView(R.layout.single_fragment_layout)
        app = intent.getParcelableExtra(APP_PARAM)
        returnToAppDetail = intent.getBooleanExtra(FROM_APP_DETAIL_PARAM, false)
        val fragment = AppsConnectionFragment.newInstance(app)
        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment).commit()
        model = TransferActivityModel(applicationInfo.loadLabel(packageManager).toString(), app, this)
    }

    override fun onPause() {
        super.onPause()
        model.onPause()
    }

    override fun onResume() {
        super.onResume()
        model.onResume()
    }

    override fun onStartConnect() {
        if (isFinished) return
        val intent = model.createTransferIntent(this)
        if (intent != null) {
            startActivityForResult(intent, model.REQUEST_CODE)
        } else {
            onConnectionError()
        }
    }

    override fun onConnectionError() {
        if (isFinished) return
        showConnectionErrorAlert()
    }

    override fun onClose() {
        if (returnToAppDetail) {
            navigator.navigateTo(app)
        } else {
            navigator.navigateTo(Navigator.Destination.MAIN_SCREEN)
        }
        finish()
    }

    override fun onStartTransferring(amount: Int) {
        val fragment = TransferringFragment.newInstance(app, amount)
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
                .replace(R.id.fragment_container, fragment).commit()
    }

    override fun onTransferFailed() {
        if (isFinished) return
        val fragment = TransferFailedFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
                .replace(R.id.fragment_container, fragment).commit()
    }

    override fun onTransferTimeout() {
        if (isFinished) return
        val fragment = TransferTimeoutFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
                .replace(R.id.fragment_container, fragment).commit()

    }

    override fun onConnected() {
        val fragment = SendAmountFragment.newInstance(app)
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
                .replace(R.id.fragment_container, fragment).commit()
    }

    override fun onTransferComplete() {
    }


    override fun onBackPressed() {
        isFinished = true
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_CANCELED) {
            model.parseCancel(intent)
        } else if (resultCode == Activity.RESULT_OK && requestCode == model.REQUEST_CODE) {
            model.parseData(this, intent)
        }
    }

    private fun showConnectionErrorAlert() {
        AlertManager.showAlert(this, R.string.transfer_connection_error_title, R.string.transfer_connection_error_message, R.string.dialog_ok, { onClose() })
    }

    companion object {
        fun getIntent(context: Context, app: EcoApplication, fromAppDetail: Boolean): Intent {
            val intent = Intent(context, TransferActivity::class.java)
            intent.putExtra(APP_PARAM, app)
            intent.putExtra(FROM_APP_DETAIL_PARAM, fromAppDetail)
            return intent
        }
    }

}

interface TransferActions {
    fun onClose()
    fun onStartConnect()
    fun onConnected()
    fun onConnectionError()
    fun onStartTransferring(amount: Int)
    fun onTransferFailed()
    fun onTransferTimeout()
    fun onTransferComplete()
}
