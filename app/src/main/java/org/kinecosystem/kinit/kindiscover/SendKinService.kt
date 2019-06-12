package org.kinecosystem.kinit.kindiscover

import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.server.NetworkServices
import org.kinecosystem.kinit.server.OperationResultCallback
import org.kinecosystem.transfer.repositories.KinTransferCallback
import org.kinecosystem.transfer.sender.service.SendKinServiceBase
import java.math.BigDecimal
import javax.inject.Inject

class SendKinService : SendKinServiceBase() {

    @Inject
    lateinit var networkServices: NetworkServices
    val address: String

    init {
        KinitApplication.coreComponent.inject(this)
        address = networkServices.walletService.userRepo.userInfo.publicAddress
    }

    @Throws(KinTransferException::class)
    override fun transferKin(toAddress: String, amount: Int, memo: String): KinTransferComplete? {
        return null
    }

    override fun transferKinAsync(toAddress: String, amount: Int, memo: String, callback: KinTransferCallback) {
        networkServices.offerService.app2appTransfer(toAddress, "14", amount, object : OperationResultCallback<String> {
            override fun onResult(result: String) {
                callback.onSuccess(KinTransferComplete(address, result, "some memo"))
            }

            override fun onError(errorCode: Int) {
                callback.onError(KinTransferException(address, "some error with error code $errorCode"))
            }
        })
    }

    @Throws(BalanceException::class)
    override fun getCurrentBalance(): BigDecimal {
        val balanceInt = networkServices.offerService.wallet.balanceInt
        return BigDecimal(balanceInt)
    }
}
