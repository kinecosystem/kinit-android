package org.kinecosystem.kinit.blockchain

import kin.base.*
import kin.sdk.AccountData
import kin.sdk.ControlledAccount
import kin.sdk.KinAccount
import kin.sdk.TransactionId
import java.math.BigDecimal

data class TopupResult(val txInfo: List<TopupTransactionInfo>, val totalAmountTransferred: Int)
data class TopupTransactionInfo(val srcAppId: String, val srcPublicAddress: String,
    val dstAppId: String, val dstPublicAddress: String, val amount: Int, val transactionId: TransactionId)

class OneWallet(private val masterAppId: String, private val masterAccount: KinAccount) {

    fun getLinkingTransactionEnvelopeFor(appId: String, publicAddress: String): String? {

        val masterAccountKeyPair = KeyPair.fromAccountId(masterAccount.publicAddress)

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
            .setMemo("link_$appId")
            .addOperation(setOptionsOperation)
            .addOperation(manageDataOperation)
            .build()
        return transaction.transactionEnvelope()
    }

    fun sendTopupTo(dstAppId: String, dstPublicAddress: String, totalAmount: Int): TopupResult {

        val allLinkedAccounts: List<ControlledAccount>? = masterAccount.controlledAccountsSync
        allLinkedAccounts
            ?.filter { it.publicAddress != dstPublicAddress }
            ?.sortedByDescending { it.balance?.value() ?: BigDecimal.ZERO }
            ?.let { linkedAccounts ->
                if (linkedAccounts.size == allLinkedAccounts.size)
                    throw IllegalAccessException(
                        "Request to topup an account that has not been linked to this master wallet")
                return topupFromLinkedAccounts(dstAppId, dstPublicAddress, totalAmount, linkedAccounts)
            } ?: return TopupResult(emptyList(), 0)
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
                srcAppId + "_Topup_$dstAppId")
                .addOperation(PaymentOperation.Builder(
                    KeyPair.fromAccountId(dstPublicAddress), AssetTypeNative(), "$amount")
                    .setSourceAccount(KeyPair.fromAccountId(srcPublicAddress))
                    .build())
                .build()
            masterAccount.sendTransactionSync(transaction)
        } catch (e: Exception) {
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

