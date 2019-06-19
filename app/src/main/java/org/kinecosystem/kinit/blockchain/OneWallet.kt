package org.kinecosystem.kinit.blockchain

import kin.base.*
import kin.sdk.AccountData
import kin.sdk.ControlledAccount
import kin.sdk.KinAccount
import kin.sdk.TransactionId
import org.kinecosystem.kinit.server.OperationResultCallback
import org.kinecosystem.kinit.util.Scheduler
import java.math.BigDecimal

data class TopupResult(val txInfo: List<TopupTransactionInfo>, val totalAmountTransferred: Int)
data class TopupTransactionInfo(val srcAppId: String, val srcPublicAddress: String,
    val dstAppId: String, val dstPublicAddress: String, val amount: Int, val transactionId: TransactionId)
data class LinkingResult(val masterPublicAddress:String, val transactionEnvelope:String)

class OneWallet(private val masterAppId: String, private val masterAccount: KinAccount,
    private val scheduler: Scheduler) {

    private var savedLinkedAccounts: List<ControlledAccount>? = null

    companion object {
        val TOPUP_ERROR_ILLEGAL_ACCESS = 0
        val TOPUP_ERROR_NO_LINKED_ACCOUNTS = 1
        val TOPUP_ERROR_UNKNOWN = 2
    }

    fun getLinkingTransactionEnvelopeFor(appId: String, publicAddress: String): LinkingResult? {
        return masterAccount.publicAddress?.let { masterAccountPublicAddress ->
            val masterAccountKeyPair = KeyPair.fromAccountId(masterAccountPublicAddress)

            val setOptionsOperation = SetOptionsOperation.Builder()
                .setSigner(Signer.ed25519PublicKey(masterAccountKeyPair), 1)
                .setSourceAccount(KeyPair.fromAccountId(publicAddress))
                .build()
            val manageDataOperation = ManageDataOperation
                .Builder(accountDataKey(publicAddress), appId.toByteArray())
                .setSourceAccount(masterAccountKeyPair)
                .build()

            val transaction = masterAccount.transactionBuilderSync
                .setFee(200)
                .setMemo(Memo.text("link_$appId"))
                .addOperation(setOptionsOperation)
                .addOperation(manageDataOperation)
                .build()


            LinkingResult(masterAccountPublicAddress, transaction.transactionEnvelope())
        }
    }

    fun totalUnifiedBalance(callback: OperationResultCallback<Int>?, refresh: Boolean = true) {
        scheduler.executeOnBackground({
            getAllLinkedAccounts(refresh)?.let { linkedAccounts ->
                val balance = linkedAccounts.sumBy { it.balance?.value()?.toInt() ?: 0 }
                scheduler.post { callback?.onResult(balance) }
            } ?: scheduler.post { callback?.onResult(0) }
        })
    }

    fun unifiedBalanceForTopup(dstPublicAddress: String, callback: OperationResultCallback<Int>?, refresh: Boolean = true) {
        scheduler.executeOnBackground({
            getAllLinkedAccounts()
                ?.filter { it.publicAddress != dstPublicAddress }
                ?.let { linkedAccounts ->
                    if (linkedAccounts.size == savedLinkedAccounts?.size)
                        scheduler.post {
                            callback?.onError(TOPUP_ERROR_ILLEGAL_ACCESS)
                        }
                    val balance = linkedAccounts.sumBy { it.balance?.value()?.toInt() ?: 0 }
                    scheduler.post { callback?.onResult(balance) }

                } ?: scheduler.post { callback?.onError(TOPUP_ERROR_NO_LINKED_ACCOUNTS) }
        })
    }

    fun sendTopupTo(dstAppId: String, dstPublicAddress: String, totalAmount: Int,
        callback: OperationResultCallback<TopupResult>?, refresh: Boolean = false) {

        scheduler.executeOnBackground({
            getAllLinkedAccounts(refresh)
                ?.filter { it.publicAddress != dstPublicAddress }
                ?.sortedByDescending { it.balance?.value() ?: BigDecimal.ZERO }
                ?.let { linkedAccounts ->
                    if (linkedAccounts.size == savedLinkedAccounts?.size)
                        scheduler.post { callback?.onError(TOPUP_ERROR_ILLEGAL_ACCESS) }
                    // "Request to topup an account that has not been linked to this master wallet")
                    val topupResult = topupFromLinkedAccounts(dstAppId, dstPublicAddress, totalAmount, linkedAccounts)
                    scheduler.post {
                        if (topupResult.totalAmountTransferred > 0) {
                            callback?.onResult(topupResult)
                        } else {
                            callback?.onError(TOPUP_ERROR_UNKNOWN)
                        }
                    }
                } ?: scheduler.post { callback?.onError(TOPUP_ERROR_NO_LINKED_ACCOUNTS) }
        })
    }

    private fun getAllLinkedAccounts(refresh: Boolean = true): List<ControlledAccount>? {
        return if (savedLinkedAccounts == null || refresh)
            masterAccount.controlledAccountsSync
        else savedLinkedAccounts
    }

    private fun topupFromLinkedAccounts(dstAppId: String, dstPublicAddress: String,
        totalAmount: Int, linkedAccounts: List<ControlledAccount>): TopupResult {
        var topupTransactions = mutableListOf<TopupTransactionInfo>()
        var amountTransferred = 0
        val masterAccountData = masterAccount.accountDataSync
        for (srcAccount in linkedAccounts) {
            if (amountTransferred == totalAmount)
                break

            val accountBalance = srcAccount.balance?.value()?.toInt() ?: 0
            val amountToTransfer = Math.min(accountBalance, totalAmount - amountTransferred)

            if (amountToTransfer > 0) {
                srcAccount.publicAddress?.let { srcPublicAddress ->
                    val srcAppId: String = masterAccountData.appIdFor(srcPublicAddress)
                    sendKin(srcAppId, srcPublicAddress, dstAppId, dstPublicAddress, amountToTransfer)
                        ?.let { transactionId ->
                            val topupTxInfo = TopupTransactionInfo(srcAppId, srcPublicAddress,
                                dstAppId, dstPublicAddress, amountToTransfer, transactionId)
                            topupTransactions.add(topupTxInfo)
                            amountTransferred += amountToTransfer
                        }
                }
            }
        }
        return TopupResult(topupTransactions, amountTransferred)
    }

    private fun sendKin(srcAppId: String?, srcPublicAddress: String, dstAppId: String, dstPublicAddress: String,
        amount: Int): TransactionId? {

        return try {
            val transaction = masterAccount.transactionBuilderSync.setFee(200).setMemo(
                Memo.text(srcAppId + "_Topup_$dstAppId"))
                .addOperation(PaymentOperation.Builder(
                    KeyPair.fromAccountId(dstPublicAddress), AssetTypeNative(), "$amount")
                    .setSourceAccount(KeyPair.fromAccountId(srcPublicAddress))
                    .build())
                .build()
            masterAccount.sendTransactionSync(transaction)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun AccountData.appIdFor(publicAddress: String): String {
        return if (publicAddress == masterAccount.publicAddress)
            masterAppId
        else this.data()[accountDataKey(publicAddress)] ?: ""
    }

    private fun accountDataKey(publicAddress: String): String {
        return "__link_$publicAddress"
    }
}

