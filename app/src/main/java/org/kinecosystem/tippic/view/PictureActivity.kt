package org.kinecosystem.tippic.view

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import org.kinecosystem.tippic.R
import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.analytics.Analytics
import org.kinecosystem.tippic.analytics.Events.Analytics.ClickEngagementPush
import org.kinecosystem.tippic.databinding.PictureActivityBinding
import org.kinecosystem.tippic.navigation.Navigator
import org.kinecosystem.tippic.repository.UserRepository
import org.kinecosystem.tippic.view.customView.AlertManager
import org.kinecosystem.tippic.viewmodel.PictureActivityViewModel
import javax.inject.Inject

class PictureActivity : BaseActivity() {

    companion object {
        const val REPORT_PUSH_ID_KEY = "report_push_id_key"
        const val REPORT_PUSH_TEXT_KEY = "report_push_text_key"

        fun getIntent(context: Context): Intent {
            return Intent(context, PictureActivity::class.java)
        }
    }

    @Inject
    lateinit var analytics: Analytics
    @Inject
    lateinit var userRepository: UserRepository

    private lateinit var model: PictureActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        TippicApplication.coreComponent.inject(this)
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<PictureActivityBinding>(this, R.layout.picture_activity)
        model = PictureActivityViewModel(Navigator(this))
        binding.model = model


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

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        handleIntentExtras(intent.extras)
    }

    private fun handleIntentExtras(intentExtras: Bundle?) {
        if (intentExtras != null && intentExtras.containsKey(REPORT_PUSH_ID_KEY)) {
            analytics.logEvent(ClickEngagementPush(intentExtras.getString(REPORT_PUSH_ID_KEY),
                    intentExtras.getString(REPORT_PUSH_TEXT_KEY)))
        }
    }

    override fun onDestroy() {

        super.onDestroy()
    }
}
