package org.kinecosystem.kinit.viewmodel.earn

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.util.Log
import android.webkit.JavascriptInterface
import com.google.gson.JsonElement
import org.kinecosystem.kinit.BuildConfig
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.network.OperationResultCallback
import org.kinecosystem.kinit.network.TaskService
import org.kinecosystem.kinit.network.Wallet
import org.kinecosystem.kinit.repository.TasksRepository
import javax.inject.Inject

abstract class WebViewModel(val navigator: Navigator) {
    abstract var interfaceName: String
    abstract var url: String
    lateinit var webFragmentActions: WebFragmentActions

    @Inject
    lateinit var tasksRepository: TasksRepository
    @Inject
    lateinit var wallet: Wallet
    @Inject
    lateinit var taskService: TaskService

    init {
        KinitApplication.coreComponent.inject(this)
        wallet.onEarnTransactionCompleted.set(false)
    }

    fun startListenToPayment() {
        wallet.listenToPayment(tasksRepository.task?.memo!!)
    }

    fun onComplete() {
        taskService.retrieveNextTask()
        navigator.navigateTo(Navigator.Destination.COMPLETE_WEB_TASK)
        if (webFragmentActions != null) {
            webFragmentActions.finish()
        }
    }

}

interface WebFragmentActions {
    fun showErrorDialog()
    fun openBrowser(url: String)
    fun finish()
}

class WebTaskTruexViewModel(val agent: String, navigator: Navigator) : WebViewModel(navigator) {
    val TRUEX_HASH: String = if (BuildConfig.DEBUG) BuildConfig.truexHashStage else BuildConfig.truexHashProd

    override var url = "file:///android_asset/truex.html?mraid=1"
    override var interfaceName: String = "Kinit"
    val USER_NET_ID_ELEMENT = "network_user_id"
    val loading: ObservableBoolean = ObservableBoolean(true)
    val javascript: ObservableField<String> = ObservableField("")

    init {
        KinitApplication.coreComponent.inject(this)
    }

    @JavascriptInterface
    fun updateStatus(status: String) {
        Log.d("###", "### got web status " + status)
    }

    @JavascriptInterface
    fun onCredit() {
        Log.d("###", "### got web credit ")
        startListenToPayment()
    }

    @JavascriptInterface
    fun onFinish() {
        Log.d("###", "### got web finish ")
        onComplete()
    }

    @JavascriptInterface
    fun onClickthrough(url: String) {
        Log.d("###", "### opening browser with url $url")
        webFragmentActions.openBrowser(url)
    }

    fun loadData() {
        taskService.retrieveTruexActivity(agent, object : OperationResultCallback<JsonElement?> {
            override fun onResult(json: JsonElement?) {
                val networkUserId = json?.asJsonObject?.get(USER_NET_ID_ELEMENT)
                if (networkUserId != null) {
                    javascript.set(
                        "updateTruexActivityData('$networkUserId', '${json.toString()}', '$TRUEX_HASH');")
                    loading.set(false)
                    //loading javascript done by binding
                }
            }

            override fun onError(errorCode: Int) {
                if (webFragmentActions != null) {
                    webFragmentActions.showErrorDialog()
                }
                Log.d("####", "#### task web model retrieveTruexActivity error")
            }
        })
    }
}