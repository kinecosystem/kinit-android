package org.kinecosystem.tippic.navigation

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import org.kinecosystem.tippic.R
import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.view.PictureActivity
import org.kinecosystem.tippic.view.RegisterErrorActivity
import org.kinecosystem.tippic.view.createWallet.CreateWalletActivity
import org.kinecosystem.tippic.view.phoneVerify.PhoneVerifyActivity

class Navigator(private val context: Context) {

    enum class Destination {
        MAIN_SCREEN, WALLET_CREATE, PHONE_VERIFY, ERROR_REGISTER
    }


    init {
        TippicApplication.coreComponent.inject(this)
    }

    fun navigateTo(dest: Destination) {
        navigateTo(dest, false, false)
    }

    fun navigateTo(dest: Destination, withSlideAnim: Boolean, reverseAnim: Boolean) {
        when (dest) {
            Destination.ERROR_REGISTER -> navigateToActivity(RegisterErrorActivity.getIntent(context), withSlideAnim, reverseAnim)
            Destination.PHONE_VERIFY -> navigateToActivity(PhoneVerifyActivity.getIntent(context), withSlideAnim, reverseAnim)
            Destination.WALLET_CREATE -> navigateToActivity(CreateWalletActivity.getIntent(context), withSlideAnim, reverseAnim)
            Destination.MAIN_SCREEN -> navigateToActivity(PictureActivity.getIntent(context), withSlideAnim, reverseAnim)
        }
    }

    private fun navigateToActivity(intent: Intent, withSlideAnimation: Boolean = true, reverse: Boolean = false) {
        context.startActivity(intent)
        if (withSlideAnimation && context is AppCompatActivity) {
            if (reverse) {
                context.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out)
            } else {
                context.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
            }
        }
    }
}