package org.kinecosystem.tippic.blockchain

import android.content.Context
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.util.Log
import kin.core.*
import kin.core.exception.*
import org.kinecosystem.tippic.BuildConfig
import org.kinecosystem.tippic.analytics.Analytics
import org.kinecosystem.tippic.analytics.Analytics.TRANSACTION_TYPE_P2P
import org.kinecosystem.tippic.analytics.Analytics.TRANSACTION_TYPE_SPEND
import org.kinecosystem.tippic.analytics.Events
import org.kinecosystem.tippic.model.KinTransaction
import org.kinecosystem.tippic.repository.DataStore
import org.kinecosystem.tippic.repository.DataStoreProvider
import org.kinecosystem.tippic.repository.UserRepository
import org.kinecosystem.tippic.server.ERROR_APP_SERVER_FAILED_RESPONSE
import org.kinecosystem.tippic.server.ERROR_EMPTY_RESPONSE
import org.kinecosystem.tippic.server.OperationCompletionCallback
import org.kinecosystem.tippic.server.api.OnboardingApi
import org.kinecosystem.tippic.server.api.WalletApi
import org.kinecosystem.tippic.util.Scheduler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal

private const val TEST_NET_URL = "https://horizon-playground.kininfrastructure.com/"
private const val MAIN_NET_URL = "https://horizon-ecosystem.kininfrastructure.com/"
private const val TEST_NET_WALLET_CACHE_NAME = "kin.app.wallet.testnet"
private const val MAIN_NET_WALLET_CACHE_NAME = "kin.app.wallet.mainnet"

private const val NETWORK_ID_MAIN = "Public Global Kin Ecosystem Network ; June 2018"
private const val NETWORK_ID_TEST = "Kin Playground Network ; June 2018"
private const val KIN_ISSUER_MAIN = "GDF42M3IPERQCBLWFEZKQRK77JQ65SCKTU3CW36HZVCX7XX5A5QXZIVK"
private const val KIN_ISSUER_STAGE = "GBC3SG6NGTSZ2OMH3FFGB7UVRQWILW367U4GSOOF4TFSZONV42UJXUH7"
private const val ACTIVE_WALLET_KEY = "activeWallet"
private const val WALLET_BALANCE_KEY = "WalletBalance"
private const val TAG = "Wallet"

class Wallet(context: Context, dataStoreProvider: DataStoreProvider,
             val userRepo: UserRepository,
             val analytics: Analytics,
             val onboardingApi: OnboardingApi,
             val walletApi: WalletApi,
             val scheduler: Scheduler) {

    enum class Type {
        Main,
        Test
    }

    private val type: Type = if (BuildConfig.DEBUG) Type.Test else Type.Main
    private val walletCache: DataStore
    private var kinClient: KinClient
    private var account: KinAccount

    val transactions: ObservableField<List<KinTransaction>> = ObservableField(ArrayList())

    init {
        val walletCacheName = if (type == Type.Test) TEST_NET_WALLET_CACHE_NAME else MAIN_NET_WALLET_CACHE_NAME
        walletCache = dataStoreProvider.dataStore(walletCacheName)
        var providerUrl = if (type == Type.Main) MAIN_NET_URL else TEST_NET_URL
        var networkId = if (type == Type.Main) NETWORK_ID_MAIN else NETWORK_ID_TEST
        val issuer = if (type == Type.Main) KIN_ISSUER_MAIN else KIN_ISSUER_STAGE

        kinClient = KinClient(context, KinitServiceProvider(providerUrl, networkId, issuer))
        account = if (kinClient.hasAccount()) {
            kinClient.getAccount(kinClient.accountCount - 1)
        } else {
            kinClient.addAccount()
        }

        userRepo.userInfo.publicAddress = account.publicAddress!!
    }

    private var activeWallet: Boolean
        set(value) {
            walletCache.putBoolean(ACTIVE_WALLET_KEY, value)
            ready.set(value)
        }
        get() = walletCache.getBoolean(ACTIVE_WALLET_KEY, false)

    var balanceInt: Int
        private set(value) {
            if (value != balanceInt) {
                walletCache.putInt(WALLET_BALANCE_KEY, value)
                balance.set(value.toString())
                logBalanceUpdated(value.toDouble())
            }
        }
        get() = walletCache.getInt(WALLET_BALANCE_KEY, 0)

    val ready: ObservableBoolean = ObservableBoolean(activeWallet)
    val balance: ObservableField<String> = ObservableField(balanceInt.toString())

    fun updateBalance(callback: ResultCallback<Balance>? = null) {
        if (ready.get()) {
            account.balance.run(object : ResultCallback<Balance> {
                override fun onResult(currentBalance: Balance) {
                    balanceInt = currentBalance.value().toInt()
                    Log.d(TAG, "#### update balance  " + balance.get())
                    callback?.onResult(currentBalance)
                }

                override fun onError(exception: java.lang.Exception) {
                    Log.d(TAG, "#### no update balance  " + balance.get())
                    analytics.logEvent(
                            Events.BILog.BalanceUpdateFailed(exception.toString() + ":" + exception.message))
                    callback?.onError(exception)
                }
            })
        }
    }


    fun retrieveTransactions(callback: OperationCompletionCallback? = null) {
        walletApi.getTransactions(userRepo.userId()).enqueue(object : Callback<WalletApi.TransactionsResponse> {
            override fun onResponse(call: Call<WalletApi.TransactionsResponse>?,
                                    response: Response<WalletApi.TransactionsResponse>?) {
                if (response != null && response.isSuccessful) {
                    Log.d(TAG, "onResponse: ${response.body()}")
                    val transactionList = response.body()
                    if (transactionList?.txs != null && transactionList.txs.isNotEmpty() && transactionList.status.equals(
                                    "ok")) {
                        injectTxsBalance(transactionList.txs)
                        transactions.set(transactionList.txs)
                    } else {
                        Log.d(TAG, "transaction list empty or null ")
                        transactions.set(ArrayList())
                    }
                    callback?.onSuccess()
                } else {
                    callback?.onError(ERROR_EMPTY_RESPONSE)
                    Log.d(TAG, "onResponse null or isSuccessful=false: $response")
                }
            }

            override fun onFailure(call: Call<WalletApi.TransactionsResponse>?, t: Throwable?) {
                Log.d(TAG, "onFailure called with throwable $t")
                callback?.onError(ERROR_APP_SERVER_FAILED_RESPONSE)
            }
        })
    }

    private fun injectTxsBalance(txs: List<KinTransaction>) {
        for (index in txs.indices) {
            if (index == 0) txs[0].txBalance = balanceInt
            else {
                val nextTx = txs[index - 1]
                val multiplier = if (nextTx.clientReceived == true) 1 else -1
                val nextBalance = nextTx.txBalance ?: 0
                val nextAmount = nextTx.amount ?: 0
                txs[index].txBalance = nextBalance - nextAmount * multiplier
            }
        }
    }

    fun updateBalanceSync() {
        try {
            val balanceResult = account.balanceSync
            scheduler.post {
                balanceInt = balanceResult.value().toInt()
                activeWallet = true
            }
        } catch (e: AccountNotFoundException) {
            if (createAccountSync()) {
                activateAccountSync()
            }
        } catch (e: AccountNotActivatedException) {
            activateAccountSync()
        } catch (e: OperationFailedException) {
            Log.e(TAG, "OperationFailedException occurred while retrieving balance ${e.message}")
            analytics.logEvent(Events.BILog.BalanceUpdateFailed(e.toString() + ":" + e.message))
        }
    }

    fun payForOrder(toAddress: String, amount: Int, orderId: String): TransactionId {
        return account.sendTransactionSync(toAddress, BigDecimal(amount), orderId)
    }

    fun initKinWallet() {
        Log.d(TAG, "### initializing Kin Wallet")
        scheduler.executeOnBackground({
            updateBalanceSync()
        })
    }

    fun importBackedUpAccount(exportedStr: String, passphrase: String): KinAccount? {
        return try {
            kinClient.importAccount(exportedStr, passphrase)
        } catch (cryptoException: CryptoException) {
            null
        } catch (createAccountException: CreateAccountException) {
            null
        }
    }

    private fun createAccountSync(): Boolean {
        var errorMessage = ""
        try {
            val call = onboardingApi.createAccount(userRepo.userId(),
                    OnboardingApi.AccountInfo(account.publicAddress!!))
            val response = call.execute()
            if (response != null && response.isSuccessful && response.body() != null) {
                val statusResponse = response.body()
                if (statusResponse?.status != "ok") {
                    errorMessage = "Response " + statusResponse?.status
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Exception occurred while creating account ${e.message}")
            errorMessage = "Exception $e with message: ${e.message}"
        }
        analytics.logEvent(
                if (errorMessage.isEmpty()) Events.BILog.StellarAccountCreationSucceeded()
                else Events.BILog.StellarAccountCreationFailed(""))

        return errorMessage.isEmpty()
    }

    private fun activateAccountSync(): Boolean {
        try {
            account.activateSync()
            analytics.logEvent(Events.Business.WalletCreated())
            scheduler.post({ activeWallet = true })
            analytics.logEvent(Events.BILog.StellarKinTrustlineSetupSucceeded())
            return true
        } catch (e: Exception) {
            Log.d(TAG, "Exception occurred while activating account ${e.message}")
            analytics.logEvent(Events.BILog.StellarKinTrustlineSetupFailed(
                    "Exception $e with message: ${e.message}"))
        }
        return false
    }

    private fun logBalanceUpdated(balance: Double) {
        analytics.setUserProperty(Events.UserProperties.BALANCE, balance)
    }

    fun logP2pTransactionCompleted(price: Int, txHash: String) {
        analytics.incrementUserProperty(Events.UserProperties.TRANSACTION_COUNT, 1)
        analytics.logEvent(Events.Business.KINTransactionSucceeded(price.toFloat(),
                txHash, TRANSACTION_TYPE_P2P))
    }

    fun logSpendTransactionCompleted(price: Int, txHash: String) {
        analytics.incrementUserProperty(Events.UserProperties.SPEND_COUNT, 1)
        analytics.incrementUserProperty(Events.UserProperties.TOTAL_KIN_SPENT,
                price.toLong())
        analytics.incrementUserProperty(Events.UserProperties.TRANSACTION_COUNT, 1)
        analytics.logEvent(Events.Business.KINTransactionSucceeded(price.toFloat(),
                txHash, TRANSACTION_TYPE_SPEND))
    }

}

class KinitServiceProvider(providerUrl: String, networkId: String, private val issuer: String) :
        ServiceProvider(providerUrl, networkId) {

    override fun getIssuerAccountId(): String {
        return issuer
    }
}