package org.kinecosystem.tippic.view.faq

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import org.kinecosystem.tippic.BuildConfig
import org.kinecosystem.tippic.R
import org.kinecosystem.tippic.databinding.FaqLayoutFragmentBinding
import org.kinecosystem.tippic.view.BaseFragment
import org.kinecosystem.tippic.viewmodel.FAQViewModel


class FAQWebFragment : BaseFragment(){
    companion object {
        fun getInstance(): FAQWebFragment {
            return FAQWebFragment()
        }
    }

    lateinit var model: FAQViewModel
    lateinit var binding: FaqLayoutFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.faq_layout_fragment, container, false)
        model = (activity as FAQActivity).getModel()
        binding.model = model
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                model.loading.set(false)
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return false
            }
        }

        if (BuildConfig.DEBUG) WebView.setWebContentsDebuggingEnabled(true)
        binding.webview.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.addJavascriptInterface(model, model.interfaceName)
        binding.webview.loadUrl(model.url)
    }
}