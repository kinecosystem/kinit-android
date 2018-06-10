package org.kinecosystem.kinit.view

import android.databinding.DataBindingUtil
import android.os.Bundle
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

    override fun moveToTutorialScreen() {
        startActivity(TutorialActivity.getIntent(this))
        finish()
    }

    private var splashViewModel: SplashViewModel? = null

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

    override fun onResume() {
        super.onResume()
        checkGoogleServices()
        splashViewModel?.onResume()
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
        splashViewModel?.onDestroy()
    }

}
