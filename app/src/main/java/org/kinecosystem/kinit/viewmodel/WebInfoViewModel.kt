package org.kinecosystem.kinit.viewmodel

import android.databinding.ObservableBoolean
import android.webkit.JavascriptInterface
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.repository.UserRepository
import javax.inject.Inject

class WebInfoViewModel(val type: Type) {

    enum class Type {
        ECO_APPS_COMING_SOON,
        ECO_APPS_LEARN_MORE
    }

    val APPS_LEARN_MORE_URL = "https://cdn.kinitapp.com/discovery/learn-more-webpage/index.html"
    val APPS_COMMING_SOON_URL = "https://cdn.kinitapp.com/discovery/coming-soon-webpage/index.html"

    @Inject
    lateinit var userRepository: UserRepository

    val loading: ObservableBoolean = ObservableBoolean(true)
    var listener: ComingSoonActions? = null
    var interfaceName: String = "Kinit"
    var url: String = when (type) {
        Type.ECO_APPS_COMING_SOON -> APPS_COMMING_SOON_URL
        else -> APPS_LEARN_MORE_URL
    }

    interface ComingSoonActions {
        fun onClose()
    }

    init {
        KinitApplication.coreComponent.inject(this)
    }

    @JavascriptInterface
    fun onClose() {
        listener?.onClose()
    }
}