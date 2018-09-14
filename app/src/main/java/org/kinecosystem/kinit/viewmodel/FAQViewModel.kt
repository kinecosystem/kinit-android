package org.kinecosystem.kinit.viewmodel

import android.webkit.JavascriptInterface
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import javax.inject.Inject

class FAQViewModel {

    var listener: FAQActions? = null
    var interfaceName: String = "Kinit"
    var url: String = "http://cdn.kinitapp.com/faq/"


    @Inject
    lateinit var analytics: Analytics

    interface FAQActions {
        fun moveBack()
        fun moveHome()
        fun pageHelpfulClicked()
        fun contactSupport()
    }

    init {
        KinitApplication.coreComponent.inject(this)
    }

    fun onHeaderButtonClicked() {
        listener?.moveHome()
    }

    fun onBackButtonClicked() {
        listener?.moveBack()
    }

    @JavascriptInterface
    fun pageLoaded(faqCategory: String?, faqTitle: String?) {
        if (faqTitle == "FAQ")
            analytics.logEvent(Events.Analytics.ViewFaqMainPage())
        else
            analytics.logEvent(Events.Analytics.ViewFaqPage(faqCategory, faqTitle))
    }

    @JavascriptInterface
    fun isPageHelpfulSelection(faqCategory: String?, faqTitle: String?, isHelpful: Boolean?) {
        listener?.pageHelpfulClicked()
        analytics.logEvent(Events.Analytics.ClickPageHelpfulButtonOnFaqPage(faqCategory, faqTitle, isHelpful))
    }

    @JavascriptInterface
    fun contactSupport(faqCategory: String?, faqTitle: String?) {
        listener?.contactSupport()
        analytics.logEvent(Events.Analytics.ClickSupportButton(faqCategory, faqTitle))
    }
}