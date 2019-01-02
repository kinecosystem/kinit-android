package org.kinecosystem.tippic.view.customView

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.databinding.BindingAdapter
import android.graphics.drawable.AnimationDrawable
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import com.airbnb.lottie.LottieAnimationView
import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.R
import org.kinecosystem.tippic.analytics.Analytics
import org.kinecosystem.tippic.analytics.Events
import org.kinecosystem.tippic.model.earn.hasPostActions
import org.kinecosystem.tippic.navigation.Navigator
import org.kinecosystem.tippic.repository.CategoriesRepository
import org.kinecosystem.tippic.util.GeneralUtils
import org.kinecosystem.tippic.view.BaseActivity
import org.kinecosystem.tippic.viewmodel.backup.BackupAlertManager
import javax.inject.Inject


class TransactionLayoutView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {


    companion object {
        @JvmStatic
        @BindingAdapter("complete")
        fun updateComplete(layoutView: TransactionLayoutView, transactionComplete: Boolean) {
            if (transactionComplete) {
                layoutView.updateComplete()
            }
        }
    }

    @Inject
    lateinit var categoriesRepository: CategoriesRepository

    @Inject
    lateinit var analytics: Analytics

    private var seenDialog = false
    private var backupAlertManager = BackupAlertManager(context)

    init {
        TippicApplication.coreComponent.inject(this)
    }

    private fun updateComplete() {
        val transactionImage = findViewById<View>(R.id.transaction_image)
        val transactionAnim = findViewById<LottieAnimationView>(R.id.transaction_anim)
        val transactionTitle = findViewById<View>(R.id.transaction_title)
        transactionImage.clearAnimation()
        categoriesRepository.currentTaskRepo?.resetTaskState()
        val animationDrawable = background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(3000)
        animationDrawable.setExitFadeDuration(1500)
        animationDrawable.start()

        transactionAnim.animate().alpha(0f).duration = 250L
        transactionTitle.animate().alpha(0f).duration = 250L
        transactionImage.animate().setStartDelay(200L).translationYBy(transactionImage.height.toFloat()).duration = 300L

        val confetti = findViewById<View>(R.id.confetti)
        val close = findViewById<View>(R.id.close_text)
        close.alpha = 0f
        close.visibility = View.VISIBLE
        confetti.scaleX = 0f
        confetti.scaleY = 0f
        confetti.visibility = View.VISIBLE
        close.animate().alpha(1f).setDuration(500L).setStartDelay(1850L + TransactionTextView.ANIM_DURATION).interpolator = AccelerateDecelerateInterpolator()
        close.setOnClickListener {
            if (context != null) {
                when {
                    shouldShowActionDialog() -> showActionDialog()
                    backupAlertManager.shouldShowAlert() -> backupAlertManager.showAlert()
                    else -> {
                        val navigator = Navigator(context)
                        navigator.navigateToCategory(categoriesRepository.currentTaskInProgress?.category_id!!, true)
                        val activity = context as BaseActivity
                        activity.finish()
                    }
                }
            }
        }
        confetti.animate().setDuration(750L).setStartDelay(100 + TransactionTextView.ANIM_DURATION).scaleY(1.2f)
                .scaleX(1.2f).setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animator: Animator) {
                        confetti.animate().setStartDelay(200).setDuration(600L).scaleY(0f)
                                .scaleX(0f).setInterpolator(AccelerateInterpolator()).setListener(null)
                    }
                }).interpolator = OvershootInterpolator(2f)
    }

    private fun shouldShowActionDialog(): Boolean {
        categoriesRepository.currentTaskInProgress?.let {
            return !seenDialog && it.hasPostActions()
        }
        return false
    }

    private fun showActionDialog() {
        categoriesRepository.currentTaskInProgress?.let {
            val taskId = it.id
            with(it.postTaskActions.orEmpty().first()) {
                AlertManager.showGeneralAlert(context, title, text, positiveText, {
                    GeneralUtils.navigateToUrl(context, url)
                    analytics.logEvent(Events.Analytics.ClickLinkButtonOnCampaignPopup(actionName, taskId))
                }, negativeText, {}, iconUrl)
                analytics.logEvent(Events.Analytics.ViewCampaignPopup(actionName, taskId))
                seenDialog = true
            }
        }
    }

}
