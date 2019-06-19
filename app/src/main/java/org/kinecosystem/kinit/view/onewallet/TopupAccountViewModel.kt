package org.kinecosystem.kinit.view.onewallet

import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.blockchain.TopupResult
import org.kinecosystem.kinit.blockchain.Wallet
import org.kinecosystem.kinit.server.OperationResultCallback
import org.kinecosystem.kinit.util.Scheduler
import javax.inject.Inject


class TopupAccountViewModel(private val dstAppId:String, private val dstPublicAddress:String, private var topupView: TopupViewActions?) {

    @Inject
    lateinit var wallet: Wallet
    @Inject
    lateinit var scheduler: Scheduler

    init {
        KinitApplication.coreComponent.inject(this)
        wallet.oneWallet.unifiedBalanceForTopup(dstPublicAddress, object: OperationResultCallback<Int> {
            override fun onError(errorCode: Int) {
            }

            override fun onResult(result: Int) {
               topupView?.updateBalance(result)
            }
        })
    }

    fun onDestroy() {
        topupView = null
    }

    fun onTransferKinButtonClicked(amount: Int) {
        topupView?.displayInProgress()
        wallet.oneWallet.sendTopupTo(dstAppId, dstPublicAddress, amount, object: OperationResultCallback<TopupResult> {
            override fun onError(errorCode: Int) {
                topupView?.displayError(errorCode)
            }

            override fun onResult(result: TopupResult) {
                topupView?.displaySuccess(result)
            }
        })
    }


}
