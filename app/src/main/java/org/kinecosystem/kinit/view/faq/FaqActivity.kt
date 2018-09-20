package org.kinecosystem.kinit.view.faq

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.util.SupportUtil
import org.kinecosystem.kinit.view.SingleFragmentActivity
import org.kinecosystem.kinit.viewmodel.FAQViewModel
import javax.inject.Inject

class FAQActivity : SingleFragmentActivity(), FAQViewModel.FAQActions {
    @Inject
    lateinit var userRepository: UserRepository

    private var webfragment: FAQWebFragment? = null

    override fun getFragment(): Fragment {
        webfragment = FAQWebFragment.getInstance()
        return webfragment as FAQWebFragment
    }

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

    override fun moveBack() {
        val webview = webfragment?.binding?.webview
        if (webview?.url == model.url)
            super.onBackPressed()
        if (webview?.url != model.url)
            webview?.loadUrl(model.url)
    }
}