package org.kinecosystem.kinit.view.migrateWallet

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.view.SingleFragmentActivity
import org.kinecosystem.kinit.view.createWallet.OnboardingCompleteFragment
import org.kinecosystem.kinit.view.support.SupportActivity
import org.kinecosystem.kinit.viewmodel.MigrateWalletViewModel
import org.kinecosystem.kinit.viewmodel.MigrateWalletEventsListener
import org.kinecosystem.kinit.viewmodel.SupportViewModel

class MigrateWalletActivity : SingleFragmentActivity(), MigrateWalletActions, MigrateWalletEventsListener {

    companion object {
        fun getIntent(context: Context) = Intent(context, MigrateWalletActivity::class.java)
    }

    private var currentFragment: Fragment? = null
    private var model: MigrateWalletViewModel = MigrateWalletViewModel()
    private val migrateWalletErrorFragment: Fragment by lazy { MigrateWalletErrorFragment.newInstance() }
    private val migrateWalletFragment: Fragment by lazy { MigrateWalletFragment.newInstance() }

    override fun beforeSuper() {
        KinitApplication.coreComponent.inject(this)
    }

    override fun init() {
        model.listener = this
    }

    override fun onBackPressed() = Unit

    override fun moveToMainScreen() {
        Navigator(this@MigrateWalletActivity).navigateTo(Navigator.Destination.MAIN_SCREEN)
        finish()
    }

    override fun getModel(): MigrateWalletViewModel {
        return model
    }

    override fun getFragment(): Fragment {
        currentFragment = migrateWalletFragment
        return migrateWalletFragment
    }

    override fun onWalletMigrated() {
        val fragment = OnboardingCompleteFragment.newInstance()
        fragment.listener = object : OnboardingCompleteFragment.AfterAnimationListener {
            override fun onAnimationEnd() {
                moveToMainScreen()
            }
        }
        replaceFragment(fragment)
    }

    override fun onMigrateWalletError() {
        if (currentFragment != migrateWalletErrorFragment) {
            currentFragment = migrateWalletErrorFragment
            replaceFragment(migrateWalletErrorFragment)
        }
    }

    override fun onWalletMigrating() {
        if (currentFragment != migrateWalletFragment) {
            currentFragment = migrateWalletFragment
            replaceFragment(migrateWalletFragment)
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

interface MigrateWalletActions {
    fun moveToMainScreen()
    fun contactSupport()
    fun getModel(): MigrateWalletViewModel
}