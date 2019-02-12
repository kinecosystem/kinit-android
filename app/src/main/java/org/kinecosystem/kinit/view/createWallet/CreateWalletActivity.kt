package org.kinecosystem.kinit.view.createWallet

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.view.SingleFragmentActivity
import org.kinecosystem.kinit.view.support.SupportActivity
import org.kinecosystem.kinit.viewmodel.CreateWalletEventsListener
import org.kinecosystem.kinit.viewmodel.CreateWalletViewModel
import org.kinecosystem.kinit.viewmodel.SupportViewModel

class CreateWalletActivity : SingleFragmentActivity(), CreateWalletActions, CreateWalletEventsListener {

    companion object {
        fun getIntent(context: Context) = Intent(context, CreateWalletActivity::class.java)
    }

    private var currentFragment: Fragment? = null
    private var model: CreateWalletViewModel = CreateWalletViewModel()
    private val createWalletErrorFragment: Fragment by lazy { CreateWalletErrorFragment.newInstance() }
    private val createWalletFragment: Fragment by lazy { CreateWalletFragment.newInstance() }

    override fun beforeSuper() {
        KinitApplication.coreComponent.inject(this)
    }

    override fun init() {
        model.listener = this
    }

    override fun onBackPressed() = Unit

    override fun moveToMainScreen() {
        Navigator(this@CreateWalletActivity).navigateTo(Navigator.Destination.MAIN_SCREEN)
        finish()
    }

    override fun getModel(): CreateWalletViewModel {
        return model
    }

    override fun getFragment(): Fragment {
        currentFragment = createWalletFragment
        return createWalletFragment
    }

    override fun onWalletCreated() {
        val fragment = OnboardingCompleteFragment.newInstance()
        fragment.listener = object : OnboardingCompleteFragment.AfterAnimationListener {
            override fun onAnimationEnd() {
                moveToMainScreen()
            }
        }
        replaceFragment(fragment)
    }

    override fun onCreateWalletError() {
        if (currentFragment != createWalletErrorFragment) {
            currentFragment = createWalletErrorFragment
            replaceFragment(createWalletErrorFragment)
        }
    }

    override fun onWalletCreating() {
        if (currentFragment != createWalletFragment) {
            currentFragment = createWalletFragment
            replaceFragment(createWalletFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        model.onDestroy()
    }

    override fun contactSupport() {
        val urlParams = mapOf("category" to "Other","subCategory" to "On-boarding error")
        val intent = SupportActivity.getIntent(this, SupportViewModel.Destination.CONTACT_US, urlParams)
        Navigator(this).navigateTo(intent)
    }
}

interface CreateWalletActions {
    fun moveToMainScreen()
    fun contactSupport()
    fun getModel(): CreateWalletViewModel
}