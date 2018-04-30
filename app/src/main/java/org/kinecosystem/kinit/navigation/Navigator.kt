package org.kinecosystem.kinit.navigation

import android.content.Context
import android.support.v7.app.AppCompatActivity
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.view.MainActivity
import org.kinecosystem.kinit.view.earn.QuestionnaireActivity
import org.kinecosystem.kinit.view.spend.PurchaseOfferActivity

class Navigator(private val context: Context) {

    enum class Destination {
        QUESTIONNAIRE, MAIN_SCREEN, SPEND
    }

    fun navigateTo(dest: Destination) {
        navigateTo(dest, 0)
    }

    fun navigateTo(dest: Destination, index: Int = 0) {
        when (dest) {
            Destination.QUESTIONNAIRE -> navigateToQuestionnaire()
            Destination.SPEND -> navigateToSpend(index)
            else -> navigateToMainScreen()
        }
    }

    private fun navigateToSpend(index: Int) {
        context.startActivity(PurchaseOfferActivity.getIntent(context, index))
        if (context is AppCompatActivity) {
            context.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
        }
    }

    private fun navigateToQuestionnaire() {
        context.startActivity(QuestionnaireActivity.getIntent(context))
        if (context is AppCompatActivity) {
            context.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
        }
    }

    private fun navigateToMainScreen() {
        context.startActivity(MainActivity.getIntent(context))
    }
}