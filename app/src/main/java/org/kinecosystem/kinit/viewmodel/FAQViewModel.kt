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
import java.lang.Exception
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
        javascript.set("submitForm('#support-data','/contact-us','/ticket-submitted.html');")
    }

    fun setJavaScript(debug: Boolean){
        val setUserId = "$('#user_id').val(\"${userRepository.userInfo.userId}\");"
        val setPlatform = "$('#platform').val(\"android: ${DeviceUtils.deviceName()}\");"
        val setVersion = "$('#version').val(\"$versionName\");"
        val setDebugMode = "$('#debug').val(\"$debug\");"
        javascript.set(setUserId + setVersion + setPlatform + setDebugMode)
    }

    fun onBackButtonClicked() {
        listener?.moveBack()
    }

    @JavascriptInterface
    fun pageLoaded(json: String) {
        val jsonObj = GeneralUtils.stringToJson(json)
        if (jsonObj != null){
            try {
                val faqCategory = jsonObj["faqCategory"] as String?
                val faqSubCategory = jsonObj["faqSubCategory"] as String?
                if (faqCategory == "FAQ")
                    analytics.logEvent(Events.Analytics.ViewFaqMainPage())
                else
                    analytics.logEvent(Events.Analytics.ViewFaqPage(faqCategory, faqSubCategory))
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    @JavascriptInterface
    fun showSubmissionError(json:String) {
        errorsCount++
        val jsonObj = GeneralUtils.stringToJson(json)
        try {
            val data = jsonObj?.get("data") as String?
            val error = jsonObj?.get("error") as String?
            Log.d("FAQViewModel", "error #$errorsCount: showing submission error $error: $data")
        } catch (e: Exception){
            e.printStackTrace()
        }
        listener?.showSubmissionError(errorsCount)
    }

    @JavascriptInterface
    fun isPageHelpfulSelection(json: String) {
        val jsonObj = GeneralUtils.stringToJson(json)
        try {
            val faqCategory = jsonObj?.get("faqCategory") as String?
            val faqSubCategory = jsonObj?.get("faqSubCategory") as String?
            val isHelpful = jsonObj?.get("isHelpful") as Boolean?
            analytics.logEvent(Events.Analytics.ClickPageHelpfulButtonOnFaqPage(faqCategory, faqSubCategory, isHelpful))
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    @JavascriptInterface
    fun contactSupport() {
        analytics.logEvent(Events.Analytics.ClickContactButtonOnSpecificFaqPage())
    }

    @JavascriptInterface
    fun supportRequestSent(json: String){
        val jsonObj = GeneralUtils.stringToJson(json)
        try {
            val faqCategory = jsonObj?.get("faqCategory") as String?
            val faqSubCategory = jsonObj?.get("faqSubCategory") as String?
            analytics.logEvent(Events.Business.SupportRequestSent(faqCategory,faqSubCategory))
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    @JavascriptInterface
    fun supportSubmitted(json: String) {
        val jsonObj = GeneralUtils.stringToJson(json)
        try {
            val faqCategory = jsonObj?.get("faqCategory") as String?
            val faqSubCategory = jsonObj?.get("faqSubCategory") as String?
            analytics.logEvent(Events.Analytics.ClickSubmitButtonOnSupportFormPage(faqCategory,faqSubCategory))
        } catch (e: Exception){
            e.printStackTrace()
        }
    }
}