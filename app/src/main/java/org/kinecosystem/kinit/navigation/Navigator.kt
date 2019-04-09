package org.kinecosystem.kinit.navigation

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.model.earn.isTaskWebView
import org.kinecosystem.kinit.model.spend.EcosystemApp
import org.kinecosystem.kinit.model.spend.Offer
import org.kinecosystem.kinit.repository.CategoriesRepository
import org.kinecosystem.kinit.util.GeneralUtils
import org.kinecosystem.kinit.view.MainActivity
import org.kinecosystem.kinit.view.RegisterErrorActivity
import org.kinecosystem.kinit.view.backup.BackupWalletActivity
import org.kinecosystem.kinit.view.bootWallet.BootWalletActivity
import org.kinecosystem.kinit.view.earn.CategoryTaskActivity
import org.kinecosystem.kinit.view.earn.QuestionnaireActivity
import org.kinecosystem.kinit.view.earn.WebTaskActivity
import org.kinecosystem.kinit.view.earn.WebTaskCompleteActivity
import org.kinecosystem.kinit.view.support.SupportActivity
import org.kinecosystem.kinit.view.phoneVerify.PhoneVerifyActivity
import org.kinecosystem.kinit.view.restore.RestoreWalletActivity
import org.kinecosystem.kinit.view.spend.AppDetailsActivity
import org.kinecosystem.kinit.view.spend.EcoAppsInfoActivity
import org.kinecosystem.kinit.view.spend.Peer2PeerActivity
import org.kinecosystem.kinit.view.spend.PurchaseOfferActivity
import org.kinecosystem.kinit.view.transfer.TransferActivity
import org.kinecosystem.kinit.view.tutorial.TutorialActivity
import org.kinecosystem.kinit.viewmodel.WebInfoViewModel.Type
import org.kinecosystem.kinit.viewmodel.SupportViewModel
import org.kinecosystem.kinit.viewmodel.bootwallet.BootAction
import javax.inject.Inject

class Navigator(private val context: Context) {

    enum class Destination {
        MAIN_SCREEN, PEER2PEER, COMPLETE_WEB_TASK, WALLET_BACKUP, WALLET_CREATE, TUTORIAL, PHONE_VERIFY, WALLET_RESTORE, FAQ, ERROR_REGISTER, ECO_APPS_COMING_SOON, ECO_APPS_LEARN_MORE,
        SUPPORT ,FEEDBACK, WALLET_MIGRATE
    }

    @Inject
    lateinit var categoriesRepository: CategoriesRepository

    init {
        KinitApplication.coreComponent.inject(this)
    }

    fun navigateTo(dest: Destination) {
        navigateTo(dest, false, false)
    }

    fun navigateTo(intent: Intent) {
        navigateToActivity(intent, false, false)
    }

    fun navigateToTask(categoryId: String) {
        categoriesRepository.getTask(categoryId)?.let {
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

    fun navigateTo(offer: Offer) {
        context.startActivity(PurchaseOfferActivity.getIntent(context, offer))
        if (context is AppCompatActivity) {
            context.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
        }
    }

    fun navigateTo(app: EcosystemApp) {
        context.startActivity(AppDetailsActivity.getIntent(context, app))
        if (context is AppCompatActivity) {
            context.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
        }
    }

    fun navigateToTransfer(app: EcosystemApp, fromAppDetail: Boolean) {
        context.startActivity(TransferActivity.getIntent(context, app, fromAppDetail))
        if (context is AppCompatActivity) {
            context.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
        }
    }


    fun navigateTo(dest: Destination, withSlideAnim: Boolean, reverseAnim: Boolean) {
        when (dest) {
            Destination.PEER2PEER -> navigateToActivity(Peer2PeerActivity.getIntent(context), withSlideAnim, reverseAnim)
            Destination.ERROR_REGISTER -> navigateToActivity(RegisterErrorActivity.getIntent(context), withSlideAnim, reverseAnim)
            Destination.COMPLETE_WEB_TASK -> navigateToActivity(WebTaskCompleteActivity.getIntent(context), withSlideAnim, reverseAnim)
            Destination.WALLET_BACKUP -> navigateToActivity(BackupWalletActivity.getIntent(context), withSlideAnim, reverseAnim)
            Destination.TUTORIAL -> navigateToActivity(TutorialActivity.getIntent(context), withSlideAnim, reverseAnim)
            Destination.PHONE_VERIFY -> navigateToActivity(PhoneVerifyActivity.getIntent(context), withSlideAnim, reverseAnim)
            Destination.WALLET_CREATE -> navigateToActivity(BootWalletActivity.getIntent(context), withSlideAnim, reverseAnim)
            Destination.WALLET_MIGRATE -> navigateToActivity(BootWalletActivity.getIntent(context, BootAction.MIGRATE), withSlideAnim, reverseAnim)
            Destination.MAIN_SCREEN -> navigateToActivity(MainActivity.getIntent(context), withSlideAnim, reverseAnim)
            Destination.WALLET_RESTORE -> navigateToActivity(RestoreWalletActivity.getIntent(context), withSlideAnim, reverseAnim)
            Destination.ECO_APPS_COMING_SOON -> navigateToActivity(EcoAppsInfoActivity.getIntent(context, Type.ECO_APPS_COMING_SOON), withSlideAnim, reverseAnim)
            Destination.ECO_APPS_LEARN_MORE -> navigateToActivity(EcoAppsInfoActivity.getIntent(context, Type.ECO_APPS_LEARN_MORE), withSlideAnim, reverseAnim)
            Destination.SUPPORT -> navigateToActivity(SupportActivity.getIntent(context), withSlideAnim, reverseAnim)
            Destination.FEEDBACK -> navigateToActivity(SupportActivity.getIntent(context, SupportViewModel.Destination.FEEDBACK), withSlideAnim, reverseAnim)
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

    fun navigateToCategory(categoryId: String, reverseAnimation: Boolean = false) {
        context.startActivity(CategoryTaskActivity.getIntent(context, categoryId))
        if (context is AppCompatActivity) {
            if (reverseAnimation) {
                context.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out)
            } else {
                context.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
            }
        }
    }

    fun navigateToUrl(url: String) {
        GeneralUtils.navigateToUrl(context, url)
    }
}