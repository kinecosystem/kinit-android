package org.kinecosystem.kinit.view.earn

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.TaskWebLayoutBinding
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.view.BaseFragment
import org.kinecosystem.kinit.viewmodel.earn.WebFragmentActions
import org.kinecosystem.kinit.viewmodel.earn.WebTaskTruexViewModel

class WebTaskTruexFragment : BaseFragment(), WebFragmentActions {
    companion object {
        fun getInstance(): WebTaskTruexFragment {
            return WebTaskTruexFragment()
        }
    }

    lateinit var trueXmodel: WebTaskTruexViewModel
    lateinit var binding: TaskWebLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.task_web_layout, container, false)

        trueXmodel = WebTaskTruexViewModel(binding.webview.settings.userAgentString, Navigator(context!!))
        trueXmodel.webFragmentActions = this
        binding.model = trueXmodel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                trueXmodel.loadData()
            }
        }
        //DEBUG
        //WebView.setWebContentsDebuggingEnabled(true)
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.addJavascriptInterface(trueXmodel, trueXmodel.interfaceName)
        binding.webview.loadUrl(trueXmodel.url)
    }

    override fun showErrorDialog() {
        //TODO update correct text
        if (activity != null) {
            val builder = AlertDialog.Builder(activity!!, R.style.CustomAlertDialog)
            builder.setTitle(R.string.general_problem_title).setMessage(
                R.string.general_problem_body).setPositiveButton(R.string.dialog_ok, { dialogInterface, i ->
                dialogInterface.dismiss()
                trueXmodel.navigator.navigateTo(Navigator.Destination.MAIN_SCREEN)
                finish()
            }).create().show()
        }
    }

    override fun finish() {
        activity?.finish()
    }
}