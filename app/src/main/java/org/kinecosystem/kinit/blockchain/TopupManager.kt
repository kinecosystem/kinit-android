package org.kinecosystem.kinit.blockchain

import kin.base.AssetTypeNative
import kin.base.KeyPair
import kin.base.PaymentOperation
import kin.sdk.KinAccount
import kin.sdk.TransactionId
import java.math.BigDecimal

class TopupManager(private val masterAppId: String, private val masterAccount: KinAccount) {

    data class TopupTransactionInfo(val srcAppId: String, val srcPublicAddress: String,
        val dstAppId: String, val dstPublicAddress: String, val amount: Int, val transactionId: TransactionId)
    data class TopupResult(val txInfo: List<TopupTransactionInfo>, val totalAmountTransferred: Int)

    fun topup(amount: Int, dstAppId: String, dstPublicAddress: String): TopupResult {
        var topupTransactions: MutableList<TopupTransactionInfo> = mutableListOf()
        var amountTransferred = 0

        try {
            val controlledAccounts = masterAccount.controlledAccountsSync
            val kinitAccountData = masterAccount.accountDataSync

            controlledAccounts.sortByDescending { it?.balance?.value() ?: BigDecimal.ZERO }
            var accountIndex = 0

            while (amountTransferred < amount && accountIndex < controlledAccounts.size) {
                if (controlledAccounts[accountIndex].publicAddress != dstPublicAddress) {
                    val controlledAccountBalance = controlledAccounts[accountIndex]?.balance?.value()?.toInt() ?: 0
                    val amountToTransfer = Math.min(controlledAccountBalance, amount-amountTransferred)
                    if (amountToTransfer > 0) {
                        controlledAccounts[accountIndex]?.publicAddress?.let { srcPublicAddress ->

                            val srcAppId: String = if (srcPublicAddress == masterAccount.publicAddress)
                                masterAppId
                            else kinitAccountData.data()["__link_$srcPublicAddress"] ?: ""

                            topupFrom(srcAppId, srcPublicAddress, dstAppId, dstPublicAddress, amountToTransfer)
                                ?.let { transactionId ->
                                   val topupTxInfo = TopupTransactionInfo(
                                       srcAppId, srcPublicAddress,
                                       dstAppId, dstPublicAddress,
                                       amountToTransfer, transactionId)
                                    topupTransactions.add(topupTxInfo)
                                    amountTransferred += amountToTransfer
                                }

                        }
                    }
                }
                accountIndex++
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return TopupResult(topupTransactions, amountTransferred)
    }

    private fun topupFrom(srcAppId: String?, srcPublicAddress: String, dstAppId: String, dstPublicAddress: String,
        amount: Int): TransactionId? {

        return try {
            val transaction = masterAccount.transactionBuilderSync.setFee(200).setMemo(srcAppId + "_Topup_$dstAppId")
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
}

