package org.kinecosystem.tippic.view.faq

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.repository.UserRepository
import org.kinecosystem.tippic.util.SupportUtil
import org.kinecosystem.tippic.view.SingleFragmentActivity
import org.kinecosystem.tippic.viewmodel.FAQViewModel
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
        TippicApplication.coreComponent.inject(this)
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