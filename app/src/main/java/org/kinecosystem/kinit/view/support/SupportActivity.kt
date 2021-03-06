package org.kinecosystem.kinit.view.support

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import org.kinecosystem.kinit.BuildConfig
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.view.SingleFragmentActivity
import org.kinecosystem.kinit.view.customView.AlertManager
import org.kinecosystem.kinit.viewmodel.SupportViewModel
import javax.inject.Inject
const val TICKET_CATEGORY = "faqCategory"
const val TICKET_SUB_CATEGORY = "faqSubCategory"
const val FAQ_IS_HELPFUL = "isHelpful"
const val TICKET_FORM_DATA = "formData"
const val TICKET_SERVER_ERROR = "serverError"

class SupportActivity : SingleFragmentActivity(), SupportViewModel.SupportActions {
    override fun getFragment(): Fragment {
        webfragment = SupportFragment.getInstance()
        return webfragment as SupportFragment
    }

    @Inject
    lateinit var userRepository: UserRepository
    private var webfragment: SupportFragment? = null

    companion object {
        fun getIntent(context: Context, destination: SupportViewModel.Destination = SupportViewModel.Destination.FAQ, urlParams: Map<String, String>? = null): Intent {
            val intent = Intent(context, SupportActivity::class.java)
            urlParams?.iterator()?.forEach { param -> intent.putExtra(param.key, param.value) }
            intent.putExtra("destination", destination.name)
            return intent
        }
    }

    init {
        KinitApplication.coreComponent.inject(this)
    }

    private var model: SupportViewModel? = null

    fun getModel(): SupportViewModel? {
        return model
    }

    override fun formPageLoaded(json: String) {
        model?.setMiscFormData(BuildConfig.DEBUG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val category = intent?.extras?.getString(TICKET_CATEGORY)
        val subCategory = intent?.extras?.getString(TICKET_SUB_CATEGORY)
        val destination = SupportViewModel.Destination.valueOf(intent?.extras?.getString("destination") ?: "FAQ")
        val urlParams = mapOf(TICKET_CATEGORY to category, TICKET_SUB_CATEGORY to subCategory)
        model = SupportViewModel(destination, urlParams)
        try {
            model?.versionName = packageManager?.getPackageInfo(packageName, 0)?.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("SupportUtil", "cant get version name " + e.message)
        }
        model?.listener = this
    }

    override fun onBackPressed() {
        moveBack()
    }

    override fun showSubmissionError(errorsCount: Number) {
        when (errorsCount){
            1 -> {
                AlertManager.showAlert(this, R.string.support_submission_error_1_title, R.string.support_submission_error_1_body, R.string.retry, {
                    model?.submitForm()
                }, R.string.close)
            }
            else -> {
                AlertManager.showAlert(this, R.string.support_submission_error_2_title, R.string.support_submission_error_2_body, R.string.close, {})
            }
        }
    }

    override fun moveBack() {
        val webview = webfragment?.binding?.webview
        if (webview?.url == userRepository.faqUrl)
            super.onBackPressed()
        else if (webview?.url?.contains("feedback") == true){
            super.onBackPressed()
        }
        if (webview?.url != userRepository.faqUrl)
            webview?.loadUrl(userRepository.faqUrl)
    }
}