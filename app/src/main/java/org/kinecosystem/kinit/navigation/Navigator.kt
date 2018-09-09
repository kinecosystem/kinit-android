package org.kinecosystem.kinit.navigation

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.model.earn.isTaskWebView
import org.kinecosystem.kinit.model.spend.Offer
import org.kinecosystem.kinit.repository.TasksRepository
import org.kinecosystem.kinit.view.MainActivity
import org.kinecosystem.kinit.view.backup.BackupWalletActivity
import org.kinecosystem.kinit.view.createWallet.CreateWalletActivity
import org.kinecosystem.kinit.view.earn.QuestionnaireActivity
import org.kinecosystem.kinit.view.earn.WebTaskActivity
import org.kinecosystem.kinit.view.earn.WebTaskCompleteActivity
import org.kinecosystem.kinit.view.phoneVerify.PhoneVerifyActivity
import org.kinecosystem.kinit.view.restore.RestoreWalletActivity
import org.kinecosystem.kinit.view.spend.Peer2PeerActivity
import org.kinecosystem.kinit.view.spend.PurchaseOfferActivity
import org.kinecosystem.kinit.view.tutorial.TutorialActivity
import javax.inject.Inject

class Navigator(private val context: Context) {

    enum class Destination {
        TASK, MAIN_SCREEN, PEER2PEER, COMPLETE_WEB_TASK, WALLET_BACKUP, WALLET_CREATE, TUTORIAL, PHONE_VERIFY, WALLET_RESTORE, INFO_TAB
    }

    @Inject
    lateinit var tasksRepository: TasksRepository

    init {
        KinitApplication.coreComponent.inject(this)
    }

    fun navigateTo(dest: Destination) {
        navigateTo(dest, 0)
    }

    fun navigateTo(offer: Offer) {
        context.startActivity(PurchaseOfferActivity.getIntent(context, offer))
        if (context is AppCompatActivity) {
            context.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
        }
    }

    fun navigateTo(dest: Destination, index: Int = 0) {
         when (dest) {
             Destination.TASK -> navigateToTask()
             Destination.PEER2PEER -> navigateToActivity(Peer2PeerActivity.getIntent(context))
             Destination.COMPLETE_WEB_TASK -> navigateToActivity(WebTaskCompleteActivity.getIntent(context))
             Destination.WALLET_BACKUP -> navigateToActivity(BackupWalletActivity.getIntent(context))
             Destination.TUTORIAL -> navigateToActivity(TutorialActivity.getIntent(context))
             Destination.PHONE_VERIFY -> navigateToActivity(PhoneVerifyActivity.getIntent(context))
             Destination.WALLET_CREATE -> navigateToActivity(CreateWalletActivity.getIntent(context))
             Destination.MAIN_SCREEN -> navigateToActivity(MainActivity.getIntent(context))
             Destination.INFO_TAB -> {
                 navigateToActivity(MainActivity.getIntent(context, 3))
             }
             Destination.WALLET_RESTORE -> navigateToActivity(RestoreWalletActivity.getIntent(context))
        }
    }

    private fun navigateToTask() {
        tasksRepository.task?.let {
            if (it.isTaskWebView()) {
                context.startActivity(WebTaskActivity.getIntent(context))
            } else {
                context.startActivity(QuestionnaireActivity.getIntent(context))
            }
        }
        if (context is AppCompatActivity) {
            context.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
        }
    }
    
    private fun navigateToActivity(intent: Intent, withSlideAnimation: Boolean = true) {
        context.startActivity(intent)
        if (withSlideAnimation && context is AppCompatActivity) {
            context.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
        }
    }
}