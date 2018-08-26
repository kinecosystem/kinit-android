package org.kinecosystem.kinit.view

import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.databinding.Observable.OnPropertyChangedCallback
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.OnboardingErrorCreatingWalletLayoutBinding
import org.kinecosystem.kinit.databinding.SplashLayoutBinding
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.util.SupportUtil
import org.kinecosystem.kinit.view.tutorial.TutorialActivity
import org.kinecosystem.kinit.viewmodel.SplashViewModel
import javax.inject.Inject


class SplashActivity : BaseActivity(), SplashNavigator {
    private companion object {
        private const val PLAY_SERVICES_UPDATE_REQUEST = 100
    }

    @Inject
    lateinit var userRepository: UserRepository

    private lateinit var splashViewModel: SplashViewModel
    private val isBLackedListListener = object : OnPropertyChangedCallback() {
        override fun onPropertyChanged(observable: Observable?, propertyId: Int) {
            if(splashViewModel.inBlackList.get()){
                showDialogBlackedList()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        KinitApplication.coreComponent.inject(this)
        super.onCreate(savedInstanceState)
        splashViewModel = SplashViewModel(this)
        moveToSplashScreen()
    }

    override fun moveToSplashScreen() {
        val binding = DataBindingUtil.setContentView<SplashLayoutBinding>(this, R.layout.splash_layout)
        binding.model = splashViewModel
    }

    override fun moveToErrorScreen() {
        val binding = DataBindingUtil.setContentView<OnboardingErrorCreatingWalletLayoutBinding>(this,
                R.layout.onboarding_error_creating_wallet_layout)
        binding.model = splashViewModel
    }

    override fun openContactSupport() {
        SupportUtil.openEmailSupport(this, userRepository)
    }

    override fun moveToMainScreen() {
        startActivity(MainActivity.getIntent(this))
        finish()
    }

    override fun onPause() {
        super.onPause()
        splashViewModel.inBlackList.removeOnPropertyChangedCallback(isBLackedListListener)
    }

    override fun onResume() {
        super.onResume()
        checkGoogleServices()
        splashViewModel.onResume()
        splashViewModel.inBlackList.addOnPropertyChangedCallback(isBLackedListListener)
        if (splashViewModel.inBlackList.get()) {
            showDialogBlackedList()
            splashViewModel.inBlackList.removeOnPropertyChangedCallback(isBLackedListListener)
        }
    }

    private fun showDialogBlackedList() {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
        var alertDialog: AlertDialog? = null
        builder.setTitle(resources.getString(R.string.oh_no))
                .setMessage(resources.getString(R.string.block_area_code_message))
                .setPositiveButton(resources.getString(R.string.dialog_ok)) { _, _ ->
                    alertDialog?.dismiss()
                    finish()
                }
        alertDialog = builder.create()
        alertDialog.show()
    }

    override fun moveToTutorialScreen() {
        startActivity(TutorialActivity.getIntent(this))
        finish()
    }

    private fun checkGoogleServices() {
        val status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(applicationContext)
        splashViewModel?.googlePlayServicesAvailable = status == ConnectionResult.SUCCESS
        if (status != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, status, PLAY_SERVICES_UPDATE_REQUEST).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        splashViewModel.inBlackList.removeOnPropertyChangedCallback(isBLackedListListener)
        splashViewModel.onDestroy()

    }

}
