package org.kinecosystem.kinit.view.transfer

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.model.spend.EcosystemApp
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.view.BaseActivity
import org.kinecosystem.kinit.view.customView.AlertManager
import org.kinecosystem.kinit.viewmodel.spend.TransferActivityModel

private const val APP_PARAM = "TransferActivity_APP_PARAM"
private const val FROM_APP_DETAIL_PARAM = "FROM_APP_DETAIL_PARAM"

class TransferActivity : BaseActivity(), TransferActions {

    lateinit var navigator: Navigator
    lateinit var model: TransferActivityModel
    lateinit var app: EcosystemApp
    private var returnToAppDetail: Boolean = false
    private var currentFragmentTag: String = ""
    private var transferAmount: Int = 0


    private val SAVE_FRAGMENT_TAG_KEY = "CURRENT_FRAGMENT_TAG"
    private val SAVE_AMOUNT_KEY = "SAVE_AMOUNT_KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        KinitApplication.coreComponent.inject(this)
        super.onCreate(savedInstanceState)
        navigator = Navigator(this)
        setContentView(R.layout.single_fragment_layout)
        app = intent.getParcelableExtra(APP_PARAM)
        returnToAppDetail = intent.getBooleanExtra(FROM_APP_DETAIL_PARAM, false)
        model = TransferActivityModel(applicationInfo.loadLabel(packageManager).toString(), app, this)
        savedInstanceState?.let {
            if (it.containsKey(SAVE_FRAGMENT_TAG_KEY)) {
                val tag = it.getString(SAVE_FRAGMENT_TAG_KEY)
                val fragment = getFragment(tag)
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment, tag).commitNowAllowingStateLoss()
                currentFragmentTag = tag
            }
        } ?: run {
            val fragment = AppsConnectionFragment.newInstance(app)
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment, AppsConnectionFragment.TAG).commitNowAllowingStateLoss()
            currentFragmentTag = AppsConnectionFragment.TAG
            model.startConnectWithDelay()
        }
    }

    private fun getFragment(tag: String): Fragment {
        var fragment = supportFragmentManager.findFragmentByTag(tag)
        //TODO
        if (fragment == null) {
            fragment = TransferringFragment.newInstance(app, 55)
        }
        return fragment
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(SAVE_FRAGMENT_TAG_KEY, currentFragmentTag)
        outState?.putInt(SAVE_AMOUNT_KEY, transferAmount)
    }


    override fun onPause() {
        super.onPause()
        model.onPause()
    }

    override fun onResume() {
        super.onResume()
        model.onResume()
    }

    override fun onDestroy() {
        model.onDestroy()
        super.onDestroy()
    }


    override fun onStartConnect() {
        val intent = model.createTransferIntent(this)
        if (intent != null) {
            startActivityForResult(intent, model.REQUEST_CODE)
        } else {
            onConnectionError()
        }
    }

    override fun onConnectionError() {
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
        transferAmount = amount
        val fragment = TransferringFragment.newInstance(app, amount)
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
                .replace(R.id.fragment_container, fragment, TransferringFragment.TAG).commit()
        currentFragmentTag = TransferringFragment.TAG
    }

    override fun onTransferFailed() {
        val fragment = TransferFailedFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
                .replace(R.id.fragment_container, fragment, TransferFailedFragment.TAG).commit()
        currentFragmentTag = TransferFailedFragment.TAG
    }

    override fun onTransferTimeout() {
        val fragment = TransferTimeoutFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
                .replace(R.id.fragment_container, fragment, TransferTimeoutFragment.TAG).commit()
        currentFragmentTag = TransferTimeoutFragment.TAG
    }

    override fun onConnected() {
        val fragment = SendAmountFragment.newInstance(app)
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
                .replace(R.id.fragment_container, fragment, SendAmountFragment.TAG).commit()
        currentFragmentTag = SendAmountFragment.TAG
    }

    override fun onTransferComplete() {
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
        fun getIntent(context: Context, app: EcosystemApp, fromAppDetail: Boolean): Intent {
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
