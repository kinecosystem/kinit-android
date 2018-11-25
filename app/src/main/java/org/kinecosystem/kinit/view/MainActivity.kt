package org.kinecosystem.kinit.view

import android.app.AlertDialog.Builder
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.main_activity.*
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.analytics.Events.Analytics.ClickEngagementPush
import org.kinecosystem.kinit.analytics.Events.Analytics.ClickMenuItem
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.repository.CategoriesRepository
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.view.BottomTabNavigation.PageSelectionListener
import org.kinecosystem.kinit.view.customView.AlertManager
import javax.inject.Inject

class MainActivity : BaseActivity(), PageSelectionListener {

    companion object {
        const val REPORT_PUSH_ID_KEY = "report_push_id_key"
        const val REPORT_PUSH_TEXT_KEY = "report_push_text_key"
        private const val SELECTED_TAB_INDEX_KEY = "selected_tab_index_key"
        private const val PRE_ANIMATION_DURATION = 500L
        private const val PRE_ANIMATION_WAIT = 2500L

        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    interface PreEarnAnimationListener : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation)
        override fun onAnimationEnd(animation: Animation)
        override fun onAnimationRepeat(animation: Animation) = Unit
    }

    @Inject
    lateinit var analytics: Analytics
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var categoriesRepository: CategoriesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        KinitApplication.coreComponent.inject(this)
        super.onCreate(savedInstanceState)
        val selectedTabIndex = savedInstanceState?.getInt(SELECTED_TAB_INDEX_KEY, 0) ?: 0
        setContentView(R.layout.main_activity)

        if (userRepository.forceUpdate) {
            AlertManager.showGeneralAlert(context = this, title = "New version available", message = "Update to the newest version to keep the kin rolling in", positiveMessage = "Update Now", positiveAction = {
                val appPackageName = packageName
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
                } catch (e: android.content.ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
                }
            }, imageRes = R.drawable.newversion_illus_popup, cancelable = false)
        }

        navigation.selectedTabIndex = selectedTabIndex
        view_pager.adapter = TabsAdapter(this, supportFragmentManager)

        if (shouldShowPreEarnAnimation()) {
            disablePageSelection()
            showPreEarnAnimation()
        } else {
            enablePageSelection()
            (container as ViewGroup).removeView(ready_to_earn_image)
        }
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        handleIntentExtras(intent.extras)
    }

    private fun shouldShowPreEarnAnimation(): Boolean {
        return categoriesRepository.hasAnyTaskAvailable()
    }

    private fun showPreEarnAnimation() {
        var textRes = R.string.ready_to_earn_some_kin
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            textRes = R.string.ready_to_earn_some_kin_compat
        }
        ready_to_earn_image.text = resources.getText(textRes)
        view_pager.visibility = View.INVISIBLE
        val slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in)
        val slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_left_out)

        slideLeftIn.duration = PRE_ANIMATION_DURATION
        slideLeftOut.duration = PRE_ANIMATION_DURATION

        slideLeftIn.interpolator = AccelerateDecelerateInterpolator()
        slideLeftOut.interpolator = slideLeftIn.interpolator

        slideLeftIn.setAnimationListener(object : PreEarnAnimationListener {
            override fun onAnimationStart(animation: Animation) {
                disablePageSelection()
            }

            override fun onAnimationEnd(animation: Animation) {
                enablePageSelection()
            }
        })
        Handler().postDelayed({
            view_pager.let {
                it.visibility = View.VISIBLE
                it.startAnimation(slideLeftIn)
            }
            ready_to_earn_image?.startAnimation(slideLeftOut)
        }, PRE_ANIMATION_WAIT)
    }

    override fun enablePageSelection() {
        navigation.setPageSelectionListener(this)
    }

    override fun disablePageSelection() {
        navigation.setPageSelectionListener(null)
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SELECTED_TAB_INDEX_KEY, navigation.selectedTabIndex)
        super.onSaveInstanceState(outState)
    }

    private fun handleIntentExtras(intentExtras: Bundle?) {
        if (intentExtras != null && intentExtras.containsKey(REPORT_PUSH_ID_KEY)) {
            analytics.logEvent(ClickEngagementPush(intentExtras.getString(REPORT_PUSH_ID_KEY),
                    intentExtras.getString(REPORT_PUSH_TEXT_KEY)))
        }
    }

    override fun onPageSelected(index: Int, tabTitle: String) {
        view_pager.setCurrentItem(index, false)
        val event = ClickMenuItem(tabTitle)
        analytics.logEvent(event)
        (view_pager.adapter as TabsAdapter).onTabVisibleToUser(index)
    }

    override fun onResume() {
        super.onResume()
        (view_pager.adapter as TabsAdapter).onTabVisibleToUser(view_pager.currentItem)

        if (userRepository.isPhoneVerificationEnabled && !userRepository.isPhoneVerified) {
            showPhoneVerifyPopup()
        }
    }

    private fun showPhoneVerifyPopup() {
        val builder = Builder(this, R.style.CustomAlertDialog)
        builder.setTitle(R.string.pop_verify_phone_title)
                .setMessage(R.string.pop_verify_phone_sub_title)
                .setPositiveButton(R.string.pop_verify_phone_possitive) { _, _ ->
                    analytics.logEvent(Events.Analytics.ClickVerifyButtonOnPhoneAuthPopup())
                    Navigator(this@MainActivity).navigateTo(Navigator.Destination.MAIN_SCREEN)
                    finish()
                }
        val alertDialog = builder.create()
        with(alertDialog) {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            show()
        }
        analytics.logEvent(Events.Analytics.ViewPhoneAuthPopup())
    }
}
