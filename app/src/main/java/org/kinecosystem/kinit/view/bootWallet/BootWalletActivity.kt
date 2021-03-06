package org.kinecosystem.kinit.view.bootWallet

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.view.SingleFragmentActivity
import org.kinecosystem.kinit.view.support.SupportActivity
import org.kinecosystem.kinit.view.support.TICKET_CATEGORY
import org.kinecosystem.kinit.view.support.TICKET_SUB_CATEGORY
import org.kinecosystem.kinit.viewmodel.SupportViewModel
import org.kinecosystem.kinit.viewmodel.bootwallet.*

class BootWalletActivity : SingleFragmentActivity() , BootWalletEventsListener {
    companion object {
        private const val BOOT_ACTION = "BOOT_ACTION"

        fun getIntent(context: Context, action: BootAction? = BootAction.CREATE) : Intent{
            val intent = Intent(context, BootWalletActivity::class.java)
            intent.putExtra(BOOT_ACTION, action?.name)
            return intent
        }
    }

    lateinit var model: BootWalletViewModel
    private lateinit var currentFragment: Fragment
    private val bootWalletFragment: Fragment by lazy { BootWalletFragment.newInstance() }

    override fun beforeSuper() {
        KinitApplication.coreComponent.inject(this)
        val action = BootAction.valueOf(intent.getStringExtra(BOOT_ACTION))
        model = when(action){
            BootAction.MIGRATE -> MigrateWalletViewModel(this)
            else -> CreateWalletViewModel(this)
        }
    }

    override fun init() {
        model.bootWallet()
    }

    override fun onBackPressed() = Unit

    fun moveToMainScreen() {
        Navigator(this@BootWalletActivity).navigateTo(Navigator.Destination.MAIN_SCREEN)
        finish()
    }

    override fun getFragment(): Fragment {
        currentFragment = bootWalletFragment
        return bootWalletFragment
    }

    override fun onWalletBooted() {
        val fragment = BootCompleteFragment.newInstance()
        fragment.listener = object : BootCompleteFragment.AfterAnimationListener {
            override fun onAnimationEnd() {
                moveToMainScreen()
            }
        }
        replaceFragment(fragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        model.onDestroy()
    }

    override fun onWalletBooting() {
        if (currentFragment != bootWalletFragment) {
            currentFragment = bootWalletFragment
            replaceFragment(bootWalletFragment)
        }
    }

    override fun contactSupport(action: BootAction) {
        val urlParams = when (action){
            BootAction.CREATE ->  mapOf(TICKET_CATEGORY to "Other",TICKET_SUB_CATEGORY to "On-boarding error")
            BootAction.MIGRATE -> mapOf(TICKET_CATEGORY to "Other",TICKET_SUB_CATEGORY to "Other")
        }
        val intent = SupportActivity.getIntent(this, SupportViewModel.Destination.CONTACT_US, urlParams)
        Navigator(this).navigateTo(intent)
    }
}

