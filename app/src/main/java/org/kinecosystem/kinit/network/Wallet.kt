package org.kinecosystem.kinit.network

import android.content.Context
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.util.Log
import kin.core.*
import kin.core.exception.AccountNotActivatedException
import kin.core.exception.AccountNotFoundException
import kin.core.exception.OperationFailedException
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Analytics.*
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.KinTransaction
import org.kinecosystem.kinit.model.Push
import org.kinecosystem.kinit.model.TaskState
import org.kinecosystem.kinit.model.spend.Coupon
import org.kinecosystem.kinit.repository.DataStore
import org.kinecosystem.kinit.repository.DataStoreProvider
import org.kinecosystem.kinit.repository.TasksRepository
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.util.Scheduler
import org.kinecosystem.kinit.viewmodel.earn.REWARD_TIMEOUT
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal

private const val TEST_NET_URL = "https://horizon-testnet.stellar.org"
private const val MAIN_NET_URL = "https://horizon.stellar.org"
private const val TEST_NET_WALLET_CACHE_NAME = "kin.app.wallet.testnet"
private const val MAIN_NET_WALLET_CACHE_NAME = "kin.app.wallet.mainnet"
private const val ACTIVE_WALLET_KEY = "activeWallet"
private const val WALLET_BALANCE_KEY = "WalletBalance"

class Wallet(context: Context, dataStoreProvider: DataStoreProvider,
    val userRepo: UserRepository,
    val tasksRepository: TasksRepository,
    val analytics: Analytics,
    val onboardingApi: OnboardingApi,
    val walletApi: WalletApi,
    val scheduler: Scheduler,
    type: Type = Type.Test) {

    enum class Type {
        Main,
        Test
    }

    private val walletCache: DataStore
    private var kinClient: KinClient
    private var account: KinAccount

    val onEarnTransactionCompleted: ObservableBoolean = ObservableBoolean(false)
    val transactions: ObservableField<ArrayList<KinTransaction>> = ObservableField(ArrayList())
    val coupons: ObservableField<ArrayList<Coupon>> = ObservableField(ArrayList())

    init {
        val walletCacheName = if (type == Type.Test) TEST_NET_WALLET_CACHE_NAME else MAIN_NET_WALLET_CACHE_NAME
        walletCache = dataStoreProvider.dataStore(walletCacheName)
        var providerUrl = if (type == Type.Main) MAIN_NET_URL else TEST_NET_URL
        var networkId = if (type == Type.Main) ServiceProvider.NETWORK_ID_MAIN else ServiceProvider.NETWORK_ID_TEST
        kinClient = KinClient(context, ServiceProvider(providerUrl, networkId))
        if (!kinClient.hasAccount()) {
            kinClient.addAccount()
        }
        account = kinClient.getAccount(0)
        userRepo.userInfo.publicAddress = account.publicAddress!!
    }

    private var activeWallet: Boolean
        set(value) {
            walletCache.putBoolean(ACTIVE_WALLET_KEY, value)
            analytics.logEvent(Events.Business.WalletCreated())
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
                    Log.d("####", "#### update balance  " + balance.get())
                    callback?.onResult(currentBalance)
                }

                override fun onError(exception: java.lang.Exception) {
                    Log.d("####", "#### no update balance  " + balance.get())
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
                    Log.d("retrieveTransactions", "onResponse: ${response.body()}")
                    val transactionList = response.body()
                    if (transactionList?.txs != null && transactionList.txs.isNotEmpty() && transactionList.status.equals(
                        "ok")) {
                        injectTxsBalance(transactionList.txs)
                        transactions.set(transactionList.txs)
                    } else {
                        Log.d("#####", "transaction list empty or null ")
                        transactions.set(ArrayList())
                    }
                    callback?.onSuccess()
                } else {
                    callback?.onError(ERROR_EMPTY_RESPONSE)
                    Log.d("retrieveTransactions", "onResponse null or isSuccessful=false: $response")
                }
            }

            override fun onFailure(call: Call<WalletApi.TransactionsResponse>?, t: Throwable?) {
                Log.d("retrieveTransactions", "onFailure called with throwable $t")
                callback?.onError(ERROR_APP_SERVER_FAILED_RESPONSE)
            }
        })
    }

    private fun injectTxsBalance(txs: ArrayList<KinTransaction>) {
        val txsOrderedTimeAsc = txs.reversed()
        for (index in txsOrderedTimeAsc.indices) {
            when (index) {
                0 -> txsOrderedTimeAsc[index].txBalance = txsOrderedTimeAsc[index].amount
                txsOrderedTimeAsc.lastIndex -> txsOrderedTimeAsc[index].txBalance = this.balanceInt
                else -> {
                    val inverter = if (txsOrderedTimeAsc[index].clientReceived == true) 1 else -1
                    val previousTransaction = txsOrderedTimeAsc[index - 1]
                    txsOrderedTimeAsc[index].txBalance = previousTransaction.txBalance?.plus(
                        inverter * (txsOrderedTimeAsc[index].amount
                            ?: 0))
                }
            }
        }
    }


    fun retrieveCoupons(callback: OperationCompletionCallback? = null) {
        walletApi.getCoupons(userRepo.userId()).enqueue(object : Callback<WalletApi.CouponsResponse> {
            override fun onResponse(call: Call<WalletApi.CouponsResponse>?,
                response: Response<WalletApi.CouponsResponse>?) {
                if (response != null && response.isSuccessful) {
                    Log.d("retrieveCoupons", "onResponse: ${response.body()}")
                    val couponsList = response.body()
                    if (couponsList?.coupons != null && couponsList.coupons.isNotEmpty() && couponsList.status.equals(
                        "ok")) {
                        coupons.set(couponsList.coupons)
                    } else {
                        Log.d("#####", "coupons® list empty or null ")
                        coupons.set(ArrayList())
                    }
                    callback?.onSuccess()
                } else {
                    Log.d("retrieveCoupons", "onResponse null or isSuccessful=false: $response")
                    callback?.onError(ERROR_EMPTY_RESPONSE)
                }
            }

            override fun onFailure(call: Call<WalletApi.CouponsResponse>?, t: Throwable?) {
                Log.d("retrieveCoupons", "onFailure called with throwable $t")
                callback?.onError(ERROR_APP_SERVER_FAILED_RESPONSE)
            }
        })
    }

    fun updateBalanceSync() {
        try {
            val balanceResult = account.balanceSync
            scheduler.post({ balanceInt = balanceResult.value().toInt() })
        } catch (e: AccountNotFoundException) {
            if (createAccountSync()) {
                activateAccountSync()
            }
        } catch (e: AccountNotActivatedException) {
            activateAccountSync()
        } catch (e: OperationFailedException) {
            Log.e("Wallet", "OperationFailedException occurred while retrieving balance ${e.message}")
            analytics.logEvent(Events.BILog.BalanceUpdateFailed(e.toString() + ":" + e.message))
        }
    }

    fun payForOrder(toAddress: String, amount: Int, orderId: String): TransactionId {
        return account.sendTransactionSync(toAddress, BigDecimal(amount), orderId)
    }

    fun initKinWallet() {
        Log.d("OnboardingService", "#### try create Wallet")
        scheduler.executeOnBackground({
            updateBalanceSync()
        })
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
            Log.d("Wallet", "Exception occurred while creating account ${e.message}")
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
            scheduler.post({ activeWallet = true })
            analytics.logEvent(Events.BILog.StellarKinTrustlineSetupSucceeded())
            return true
        } catch (e: Exception) {
            Log.d("Wallet", "Exception occurred while activating account ${e.message}")
            analytics.logEvent(Events.BILog.StellarKinTrustlineSetupFailed(
                "Exception $e with message: ${e.message}"))
        }
        return false
    }

    private fun logBalanceUpdated(balance: Double) {
        analytics.setUserProperty(Events.UserProperties.BALANCE, balance)
    }

    fun logP2pTransactionCompleted(price: Int, orderId: String) {
        analytics.incrementUserProperty(Events.UserProperties.TRANSACTION_COUNT, 1)
        analytics.logEvent(Events.Business.KINTransactionSucceeded(price.toFloat(),
            orderId, TRANSACTION_TYPE_P2P))
    }

    fun logSpendTransactionCompleted(price: Int, orderId: String) {
        analytics.incrementUserProperty(Events.UserProperties.SPEND_COUNT, 1)
        analytics.incrementUserProperty(Events.UserProperties.TOTAL_KIN_SPENT,
            price.toLong())
        analytics.incrementUserProperty(Events.UserProperties.TRANSACTION_COUNT, 1)
        analytics.logEvent(Events.Business.KINTransactionSucceeded(price.toFloat(),
            orderId, TRANSACTION_TYPE_SPEND))
    }

    fun logEarnTransactionCompleted(price: Int, orderId: String) {
        analytics.incrementUserProperty(Events.UserProperties.EARN_COUNT, 1)
        analytics.incrementUserProperty(Events.UserProperties.TOTAL_KIN_EARNED,
            price.toLong())
        analytics.incrementUserProperty(Events.UserProperties.TRANSACTION_COUNT, 1)
        analytics.logEvent(Events.Business.KINTransactionSucceeded(price.toFloat(),
            orderId, TRANSACTION_TYPE_EARN))
    }

    fun listenToPayment(memo: String) {
        val paymentWatcher = account.createPaymentWatcher()
        paymentWatcher.start { payment ->
            if (payment.memo() == memo) {
                updateBalanceForPayment(payment)
                paymentWatcher.stop()
            }
        }
        scheduler.scheduleOnMain({
            if (!onEarnTransactionCompleted.get()) {
                updateBalanceForPayment()
                paymentWatcher.stop()
            }
        }, REWARD_TIMEOUT)
    }

    private fun updateBalanceForPayment(payment: PaymentInfo? = null) {
        updateBalance(object : ResultCallback<Balance> {
            override fun onResult(balance: Balance) {
                if (payment != null) {
                    onEarnTransactionCompleted.set(true)
                    setTaskState(TaskState.TRANSACTION_COMPLETED)
                    retrieveTransactions()
                    logEarnTransactionCompleted(payment.amount().toInt(), payment.hash().id())
                } else {
                    setTaskState(TaskState.TRANSACTION_ERROR)
                }
            }

            override fun onError(e: Exception) {
                setTaskState(TaskState.TRANSACTION_ERROR)
                if (payment != null) {
                    logEarnTransactionCompleted(payment.amount().toInt(), payment.hash().id())
                }
            }
        })
    }


    fun onTransactionMessageReceived(transactionComplete: Push.TransactionCompleteMessage) {

        if (!isValid(transactionComplete)) {
            setTaskState(TaskState.TRANSACTION_ERROR)
            return
        }

        updateBalance(object : ResultCallback<Balance> {
            override fun onResult(balance: Balance) {
                if (transactionComplete.kin != null && transactionComplete.txHash != null) {
                    onEarnTransactionCompleted.set(true)
                    setTaskState(TaskState.TRANSACTION_COMPLETED)
                    retrieveTransactions()
                    logEarnTransactionCompleted(transactionComplete.kin, transactionComplete.txHash)
                } else {
                    setTaskState(TaskState.TRANSACTION_ERROR)
                }
            }

            override fun onError(e: Exception) {
                setTaskState(TaskState.TRANSACTION_ERROR)
                if (transactionComplete.kin != null && transactionComplete.txHash != null) {
                    logEarnTransactionCompleted(transactionComplete.kin, transactionComplete.txHash)
                }
            }
        })

        Log.d("###", "#### KinMessagingService transactionComplete: $transactionComplete")
    }


    private fun isValid(transactionComplete: Push.TransactionCompleteMessage): Boolean {
        return transactionComplete.kin != null &&
            transactionComplete.kin > 0 &&
            transactionComplete.userId != null &&
            transactionComplete.userId == userRepo.userInfo.userId
    }

    private fun setTaskState(state: Int) {
        tasksRepository.taskState = state
    }

}