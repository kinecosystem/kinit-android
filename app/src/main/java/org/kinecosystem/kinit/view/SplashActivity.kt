package org.kinecosystem.kinit.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.OnboardingErrorCreatingWalletLayoutBinding
import org.kinecosystem.kinit.databinding.SplashLayoutBinding
import org.kinecosystem.kinit.util.SupportUtil
import org.kinecosystem.kinit.view.tutorial.TutorialActivity
import org.kinecosystem.kinit.viewmodel.SplashViewModel

class SplashActivity : BaseActivity(), SplashNavigator {
    override fun moveToTutorialScreen() {
        startActivity(TutorialActivity.getIntent(this))
        finish()
    }

    private var splashViewModel: SplashViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashViewModel = SplashViewModel(coreComponents, this)
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
        SupportUtil.openEmailSupport(this, coreComponents.userRepo())
    }

    override fun moveToMainScreen() {
        startActivity(MainActivity.getIntent(this))
        finish()
    }

    override fun onResume() {
        super.onResume()
        splashViewModel?.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        splashViewModel?.onDestroy()
    }

}
