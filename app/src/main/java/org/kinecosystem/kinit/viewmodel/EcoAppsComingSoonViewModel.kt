package org.kinecosystem.kinit.viewmodel

import android.databinding.ObservableBoolean
import android.webkit.JavascriptInterface
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.repository.UserRepository
import javax.inject.Inject

class EcoAppsComingSoonViewModel {


    @Inject
    lateinit var userRepository: UserRepository

    val loading: ObservableBoolean = ObservableBoolean(true)
    var listener: ComingSoonActions? = null
    var interfaceName: String = "Kinit"
    var url: String = ""

    interface ComingSoonActions {
        fun onClose()
    }

    init {
        KinitApplication.coreComponent.inject(this)
        url = userRepository.comingSoonUrl
    }

    @JavascriptInterface
    fun onClose() {
        listener?.onClose()
    }
}