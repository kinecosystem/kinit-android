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
import org.kinecosystem.kinit.util.GeneralUtils
import org.kinecosystem.kinit.view.support.*
import javax.inject.Inject

class SupportViewModel(val destination: Destination, urlParams: Map<String, String?>?) {

    @Inject
    lateinit var userRepository: UserRepository

    enum class Destination {
        FAQ,
        CONTACT_US,
        FEEDBACK

    }

    class FAQAnalyticsData(json: String){
        private val jsonObj = GeneralUtils.stringToJson(json)
        var faqCategory: String? = null
        var faqSubCategory: String? = null
        var isHelpful: Boolean? = null
        var formData: String? = null
        var serverError: String? = null

        init {
            faqCategory = get(TICKET_CATEGORY)
            faqSubCategory = get(TICKET_SUB_CATEGORY)
            isHelpful = get(FAQ_IS_HELPFUL)
            formData = get(TICKET_FORM_DATA)
            serverError = get(TICKET_SERVER_ERROR)
        }

        fun <T: Any> get(name: String): T?{
            try {
                return jsonObj?.get(name) as T?
            } catch (e: Exception){
                Log.i("FAQAnalyticsData", "$name was not found")
            }
            return null
        }
    }

    var errorsCount = 0
    var versionName: String? = null
    val loading: ObservableBoolean = ObservableBoolean(true)
    val jsToRun: ObservableField<String> = ObservableField("")
    var listener: SupportActions? = null
    var interfaceName: String = "Kinit"
    var url: String = ""


    @Inject
    lateinit var analytics: Analytics

    interface SupportActions {
        fun moveBack()
        fun showSubmissionError(errorsCount: Number)
        fun formPageLoaded(json: String)
    }

    init {
        KinitApplication.coreComponent.inject(this)
        url = when(destination){
            Destination.FAQ -> userRepository.faqUrl
            Destination.CONTACT_US -> userRepository.contactUsUrl + "?category=${urlParams?.get(TICKET_CATEGORY)}&sub_category=${urlParams?.get(TICKET_SUB_CATEGORY)}"
            Destination.FEEDBACK -> userRepository.feedbackUrl
        }
    }

    fun submitForm(){
        jsToRun.set("submitForm();")
    }

    fun setMiscFormData(debug: Boolean){
        val userId = "\"${userRepository.userInfo.userId}\""
        val platform = "\"android: ${DeviceUtils.deviceName()}\""
        val version = "\"$versionName\""
        val isDebug = "\"$debug\""
        jsToRun.set("window.setMiscFormData($userId, $platform, $version, $isDebug);")
    }

    fun onBackButtonClicked() {
        listener?.moveBack()
    }

    @JavascriptInterface
    fun pageLoaded(json: String) {
        val eventData = FAQAnalyticsData(json)
        if (eventData.faqCategory == "FAQ")
            analytics.logEvent(Events.Analytics.ViewFaqMainPage())
        else
            analytics.logEvent(Events.Analytics.ViewFaqPage(eventData.faqCategory, eventData.faqSubCategory))
    }

    @JavascriptInterface
    fun formPageLoaded(json: String) {
        listener?.formPageLoaded(json)
    }

    @JavascriptInterface
    fun showSubmissionError(json:String) {
        errorsCount++
        val eventData = FAQAnalyticsData(json)
        Log.d("SupportViewModel", "error #$errorsCount: showing submission error ${eventData.serverError}: ${eventData.formData}")
        listener?.showSubmissionError(errorsCount)
    }

    @JavascriptInterface
    fun isPageHelpfulSelection(json: String) {
        val eventData = FAQAnalyticsData(json)
        analytics.logEvent(Events.Analytics.ClickPageHelpfulButtonOnFaqPage(eventData.faqCategory, eventData.faqSubCategory, eventData.isHelpful))

    }

    @JavascriptInterface
    fun contactSupport(json: String) {
        val eventData = FAQAnalyticsData(json)
        analytics.logEvent(Events.Analytics.ClickContactButtonOnSpecificFaqPage(eventData.faqCategory, eventData.faqSubCategory))
    }

    @JavascriptInterface
    fun supportRequestSent(json: String){
        val eventData = FAQAnalyticsData(json)
        analytics.logEvent(Events.Business.SupportRequestSent(eventData.faqCategory, eventData.faqSubCategory))
    }

    @JavascriptInterface
    fun supportSubmitted(json: String) {
        val eventData = FAQAnalyticsData(json)
        analytics.logEvent(Events.Analytics.ClickSubmitButtonOnSupportFormPage(eventData.faqCategory, eventData.faqSubCategory))
    }


    @JavascriptInterface
    fun feedbackFormSent(){
        analytics.logEvent(Events.Business.FeedbackformSent())
    }

    @JavascriptInterface
    fun feedbackSubmitted() {
        analytics.logEvent(Events.Analytics.ClickSubmitButtonOnFeedbackForm())
    }
}