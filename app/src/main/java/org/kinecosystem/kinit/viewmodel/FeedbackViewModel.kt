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

class FeedbackViewModel {

    @Inject
    lateinit var userRepository: UserRepository

    var errorsCount = 0
    var versionName: String? = null
    val loading: ObservableBoolean = ObservableBoolean(true)
    val javascript: ObservableField<String> = ObservableField("")
    var listener: FeedbackActions? = null
    var interfaceName: String = "Kinit"
    var url: String = ""


    @Inject
    lateinit var analytics: Analytics

    interface FeedbackActions {
        fun moveHome()
        fun showSubmissionError(errorsCount: Number)
    }

    init {
        KinitApplication.coreComponent.inject(this)
        url = userRepository.feedbackUrl
    }

    fun submitForm(){
        javascript.set("submitForm('#feedback-data','/feedback','/feedback-submitted.html');")
    }

    fun onHeaderButtonClicked() {
        listener?.moveHome()
    }

    fun onBackButtonClicked() {
        listener?.moveHome()
    }

    fun setJavaScript(debug: Boolean){
        javascript.set("$('#user_id').val(\"${userRepository.userInfo.userId}\");")
        javascript.set("$('#platform').val(\"android: ${DeviceUtils.deviceName()}\");")
        javascript.set("$('#version').val(\"$versionName\");")
        javascript.set("$('#debug').val(\"$debug\");")
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
    fun feedbackFormSent(json: String){
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
    fun feedbackSubmitted(json: String) {
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