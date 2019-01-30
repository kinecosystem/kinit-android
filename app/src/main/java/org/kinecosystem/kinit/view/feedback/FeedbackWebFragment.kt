package org.kinecosystem.kinit.view.feedback

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import org.kinecosystem.kinit.BuildConfig
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.FeedbackLayoutFragmentBinding
import org.kinecosystem.kinit.view.BaseFragment
import org.kinecosystem.kinit.viewmodel.FeedbackViewModel


class FeedbackWebFragment : BaseFragment(){
    companion object {
        fun getInstance(): FeedbackWebFragment {
            return FeedbackWebFragment()
        }
    }

    lateinit var model: FeedbackViewModel
    lateinit var binding: FeedbackLayoutFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.feedback_layout_fragment, container, false)
        model = (activity as FeedbackActivity).getModel()
        binding.model = model
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                try {
                    model.versionName = context?.packageManager
                            ?.getPackageInfo(context?.packageName, 0)?.versionName
                } catch (e: PackageManager.NameNotFoundException) {
                    Log.e("SupportUtil", "cant get version name " + e.message)
                }
                if (url?.contains(model.userRepository.feedbackUrl) == true)
                    model.setJavaScript(BuildConfig.DEBUG)
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