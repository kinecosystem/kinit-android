package org.kinecosystem.kinit.view.faq

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.util.SupportUtil
import org.kinecosystem.kinit.view.SingleFragmentActivity
import org.kinecosystem.kinit.view.customView.AlertManager
import org.kinecosystem.kinit.viewmodel.FAQViewModel
import javax.inject.Inject

class FAQActivity : SingleFragmentActivity(), FAQViewModel.FAQActions {

    override fun getFragment(): Fragment {
        webfragment = FAQWebFragment.getInstance()
        return webfragment as FAQWebFragment
    }

    @Inject
    lateinit var userRepository: UserRepository
    private var webfragment: FAQWebFragment? = null

    companion object {
        fun getIntent(context: Context) = Intent(context, FAQActivity::class.java)
    }

    init {
        KinitApplication.coreComponent.inject(this)
    }

    private var model: FAQViewModel = FAQViewModel()

    fun getModel(): FAQViewModel {
        return model
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model.listener = this
    }

    override fun moveHome() {
        webfragment?.binding?.webview?.loadUrl(model.url)
    }

    override fun contactSupport() {
        SupportUtil.openEmail(this, userRepository, SupportUtil.Type.SUPPORT)
    }

    override fun onBackPressed() {
        moveBack()
    }

    override fun showSubmissionError(errorsCount: Number) {
        when (errorsCount){
            1 -> {
                AlertManager.showAlert(this, R.string.support_submission_error_1_title, R.string.support_submission_error_1_body, R.string.retry, {
                    model.submitForm()
                }, R.string.close)
            }
            else -> {
                AlertManager.showAlert(this, R.string.support_submission_error_2_title, R.string.support_submission_error_2_body, R.string.close, {})
            }
        }
    }

    override fun moveBack() {
        val webview = webfragment?.binding?.webview
        if (webview?.url == model.url)
            super.onBackPressed()
        if (webview?.url != model.url)
            webview?.loadUrl(model.url)
    }
}