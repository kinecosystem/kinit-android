package org.kinecosystem.kinit.view.spend

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import org.kinecosystem.kinit.BuildConfig
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.ComingSoonWebLayoutBinding
import org.kinecosystem.kinit.view.BaseFragment
import org.kinecosystem.kinit.viewmodel.WebInfoViewModel


class EcoAppsInfoWebFragment : BaseFragment(){

    companion object {
        fun getInstance(): EcoAppsInfoWebFragment {
            return EcoAppsInfoWebFragment()
        }
    }

    lateinit var model: WebInfoViewModel
    lateinit var binding: ComingSoonWebLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.coming_soon_web_layout, container, false)
        model = (activity as EcoAppsInfoActivity).getModel()
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
        }

        if (BuildConfig.DEBUG) WebView.setWebContentsDebuggingEnabled(true)
        binding.webview.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.addJavascriptInterface(model, model.interfaceName)
        binding.webview.loadUrl(model.url)
    }
}