package org.kinecosystem.kinit.view

import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.viewmodel.SplashViewModel


class SplashActivity : BaseActivity(), SplashViewModel.ISplashViewModel {

    private companion object {
        private const val PLAY_SERVICES_UPDATE_REQUEST = 100
        val TAG: String = SplashActivity::class.java.simpleName
    }

    private var googlePlayServicesAvailable: Int? = null
    private val splashViewModel: SplashViewModel = SplashViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        KinitApplication.coreComponent.inject(this)
        super.onCreate(savedInstanceState)
        googlePlayServicesAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(applicationContext)
        setContentView(R.layout.splash_layout)
    }

    override fun onResume() {
        super.onResume()
        if (googlePlayServicesAvailable == ConnectionResult.SUCCESS) {
            splashViewModel.listener = this
            splashViewModel.onResume()
        } else {
            GoogleApiAvailability
                    .getInstance()
                    .getErrorDialog(
                            this,
                            googlePlayServicesAvailable
                                    ?: -1, SplashActivity.PLAY_SERVICES_UPDATE_REQUEST)
                    .show()
        }
    }

    override fun moveToNextScreen(nextScreen: Navigator.Destination) {
        Navigator(this@SplashActivity).navigateTo(nextScreen)
        finish()
    }

    override fun onPause() {
        super.onPause()
        splashViewModel.listener = null
    }

    override fun showBlackListDialog() {
        val builder = AlertDialog.Builder(this@SplashActivity, R.style.CustomAlertDialog)
        var alertDialog: AlertDialog? = null
        builder.setTitle(resources.getString(R.string.oh_no))
                .setMessage(resources.getString(R.string.block_area_code_message))
                .setPositiveButton(resources.getString(R.string.dialog_ok)) { _, _ ->
                    alertDialog?.dismiss()
                    finish()
                }
                .setCancelable(false)
        alertDialog = builder.create()
        alertDialog.show()
    }
}