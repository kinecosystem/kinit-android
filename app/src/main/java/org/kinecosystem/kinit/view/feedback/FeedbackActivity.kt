package org.kinecosystem.kinit.view.feedback

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.view.SingleFragmentActivity
import org.kinecosystem.kinit.view.customView.AlertManager
import org.kinecosystem.kinit.viewmodel.FeedbackViewModel
import javax.inject.Inject

class FeedbackActivity : SingleFragmentActivity(), FeedbackViewModel.FeedbackActions {

    override fun getFragment(): Fragment {
        webfragment = FeedbackWebFragment.getInstance()
        return webfragment as FeedbackWebFragment
    }

    @Inject
    lateinit var userRepository: UserRepository
    private var webfragment: FeedbackWebFragment? = null

    companion object {
        fun getIntent(context: Context) = Intent(context, FeedbackActivity::class.java)
    }

    init {
        KinitApplication.coreComponent.inject(this)
    }

    private var model: FeedbackViewModel = FeedbackViewModel()

    fun getModel(): FeedbackViewModel {
        return model
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model.listener = this
    }

    override fun moveHome() {
        webfragment?.binding?.webview?.loadUrl(model.url)
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
}