package org.kinecosystem.kinit.view

import android.app.AlertDialog.Builder
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.AttributeSet
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
import org.kinecosystem.kinit.repository.TasksRepository
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.view.BottomTabNavigation.PageSelectionListener
import javax.inject.Inject

class MainActivity : BaseActivity(), PageSelectionListener {

    companion object {
        const val REPORT_PUSH_ID_KEY = "report_push_id_key"
        const val REPORT_PUSH_TEXT_KEY = "report_push_text_key"
        private const val SELECTED_TAB_INDEX_KEY = "selected_tab_index_key"
        private const val PRE_ANIMATION_DURATION = 500L
        private const val PRE_ANIMATION_WAIT = 2500L

        fun getIntent(context: Context, tabIndex: Int = 0): Intent {
            var intent = Intent(context, MainActivity::class.java)
            intent.putExtra(SELECTED_TAB_INDEX_KEY, tabIndex)
            return intent
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
    lateinit var taskRepository: TasksRepository

    override fun onCreateView(name: String?, context: Context?, attrs: AttributeSet?): View {
        return super.onCreateView(name, context, attrs)
        navigation.selectedTabIndex = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        KinitApplication.coreComponent.inject(this)
        super.onCreate(savedInstanceState)
        val selectedTabIndex = savedInstanceState?.getInt(SELECTED_TAB_INDEX_KEY, 0) ?: intent?.extras?.getInt(SELECTED_TAB_INDEX_KEY, 0) ?: 0
        setContentView(R.layout.main_activity)

        view_pager.adapter = TabsAdapter(this)
        navigation.selectedTabIndex = selectedTabIndex


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
        return taskRepository.isTaskAvailable() && !taskRepository.isTaskStarted.get()
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
