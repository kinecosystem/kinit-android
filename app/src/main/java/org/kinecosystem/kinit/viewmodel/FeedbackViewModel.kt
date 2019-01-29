package org.kinecosystem.kinit.viewmodel

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.webkit.JavascriptInterface
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.util.DeviceUtils
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
        javascript.set("$.submitForm('/feedback','/feedback-submitted.html');")
    }

    fun onHeaderButtonClicked() {
        listener?.moveHome()
    }

    fun setJavaScript(debug: Boolean){
        javascript.set("$.setMiscData(\"${userRepository.userInfo.userId}\",\"android: ${DeviceUtils.deviceName()}\",\"$versionName\",\"$debug\");")
    }

    fun onBackButtonClicked() {
        listener?.moveHome()
    }

    @JavascriptInterface
    fun showSubmissionError(data: String) {
        errorsCount++
        listener?.showSubmissionError(errorsCount)
    }
}