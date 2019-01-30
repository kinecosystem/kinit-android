package org.kinecosystem.kinit.viewmodel

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.util.Log
import android.webkit.JavascriptInterface
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.util.DeviceUtils
import javax.inject.Inject

class FAQViewModel {

    @Inject
    lateinit var userRepository: UserRepository

    var errorsCount = 0
    var versionName: String? = null
    val loading: ObservableBoolean = ObservableBoolean(true)
    val javascript: ObservableField<String> = ObservableField("")
    var listener: FAQActions? = null
    var interfaceName: String = "Kinit"
    var url: String = ""


    @Inject
    lateinit var analytics: Analytics

    interface FAQActions {
        fun moveBack()
        fun showSubmissionError(errorsCount: Number)
    }

    init {
        KinitApplication.coreComponent.inject(this)
        url = userRepository.faqUrl
    }

    fun submitForm(){
        javascript.set("$.submitForm('#support-data','/contact-us','/ticket-submitted.html');")
    }

    fun setJavaScript(debug: Boolean){
        javascript.set("$.setMiscData(\"${userRepository.userInfo.userId}\",\"android: ${DeviceUtils.deviceName()}\",\"$versionName\",\"$debug\");")
    }

    fun onBackButtonClicked() {
        listener?.moveBack()
    }

    @JavascriptInterface
    fun pageLoaded(faqCategory: String, faqSubCategory: String) {
        if (faqSubCategory == "FAQ")
            analytics.logEvent(Events.Analytics.ViewFaqMainPage())
        else
            analytics.logEvent(Events.Analytics.ViewFaqPage(faqCategory, faqSubCategory))
    }

    @JavascriptInterface
    fun showSubmissionError(data: String) {
        errorsCount++
        Log.d("FAQViewModel", "error #$errorsCount: showing submission error $data")
        listener?.showSubmissionError(errorsCount)
    }

    @JavascriptInterface
    fun isPageHelpfulSelection(faqCategory: String, faqTitle: String, isHelpful: Boolean) {
        analytics.logEvent(Events.Analytics.ClickPageHelpfulButtonOnFaqPage(faqCategory, faqTitle, isHelpful))
    }


    @JavascriptInterface
    fun contactSupport(faqCategory: String, faqTitle: String) {
        analytics.logEvent(Events.Analytics.ClickContactButtonOnSpecificFaqPage())
    }
}