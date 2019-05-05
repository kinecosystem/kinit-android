package org.kinecosystem.kinit.blockchain

import android.content.Context
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.util.Log
import kin.sdk.*
import kin.sdk.exception.AccountNotFoundException
import kin.sdk.exception.CreateAccountException
import kin.sdk.exception.CryptoException
import kin.sdk.exception.OperationFailedException
import kin.sdk.migration.MigrationManager
import kin.sdk.migration.MigrationNetworkInfo
import kin.sdk.migration.common.KinSdkVersion
import kin.sdk.migration.common.interfaces.*
import kin.utils.ResultCallback
import org.kinecosystem.kinit.BuildConfig
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Analytics.*
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.KinTransaction
import org.kinecosystem.kinit.model.Push
import org.kinecosystem.kinit.model.TaskState
import org.kinecosystem.kinit.model.spend.Coupon
import org.kinecosystem.kinit.repository.CategoriesRepository
import org.kinecosystem.kinit.repository.DataStore
import org.kinecosystem.kinit.repository.DataStoreProvider
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.server.ERROR_APP_SERVER_FAILED_RESPONSE
import org.kinecosystem.kinit.server.ERROR_EMPTY_RESPONSE
import org.kinecosystem.kinit.server.OperationCompletionCallback
import org.kinecosystem.kinit.server.api.OnboardingApi
import org.kinecosystem.kinit.server.api.WalletApi
import org.kinecosystem.kinit.util.Scheduler
import org.kinecosystem.kinit.viewmodel.earn.REWARD_TIMEOUT
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal


private const val CORE_TEST_NETWORK_URL = "https://horizon-playground.kininfrastructure.com/"
private const val CORE_TEST_NETWORK_ID = "Kin Playground Network ; June 2018"
private const val CORE_MAIN_NETWORK_URL = "https://horizon-ecosystem.kininfrastructure.com/"
private const val CORE_MAIN_NETWORK_ID = "Public Global Kin Ecosystem Network ; June 2018"
private const val CORE_ISSUER_MAIN = "GDF42M3IPERQCBLWFEZKQRK77JQ65SCKTU3CW36HZVCX7XX5A5QXZIVK"
private const val CORE_ISSUER_TEST = "GBC3SG6NGTSZ2OMH3FFGB7UVRQWILW367U4GSOOF4TFSZONV42UJXUH7"
private const val SDK_TEST_NETWORK_URL = "https://horizon-testnet.kininfrastructure.com/"
private const val SDK_TEST_NETWORK_ID = "Kin Testnet ; December 2018"
private const val SDK_MAIN_NETWORK_URL = "https://horizon.kinfederation.com/"
private const val SDK_MAIN_NETWORK_ID = "Kin Mainnet ; December 2018"
private const val MIGRATE_ACCOUNT_SERVICE_TEST_URL = "https://stage.kinitapp.com/user/migrate?"
private const val MIGRATE_ACCOUNT_SERVICE_PRODUCTION_URL = "https://api3.kinitapp.com/user/migrate?"

private const val TEST_NET_WALLET_CACHE_NAME = "kin.app.wallet.testnet"
private const val MAIN_NET_WALLET_CACHE_NAME = "kin.app.wallet.mainnet"
private const val ACTIVE_WALLET_KEY = "activeWallet"
private const val IS_KIN3 = "IS_KIN3"
private const val WALLET_BALANCE_KEY = "WalletBalance"
private const val TAG = "Wallet"
private const val KINIT_APP_ID = "kit"

class Wallet(context: Context, dataStoreProvider: DataStoreProvider,
             val userRepo: UserRepository,
             val categoriesRepository: CategoriesRepository,
             val analytics: Analytics,
             val onboardingApi: OnboardingApi,
             val walletApi: WalletApi,
             val scheduler: Scheduler) {

    enum class Type {
        Main,
        Test
    }

    private val walletCache: DataStore
    private var kinClient: KinClient
    private var account: KinAccount
    private lateinit var paymentListener: ListenerRegistration
    private val applicationContext = context.applicationContext

    val onEarnTransactionCompleted: ObservableBoolean = ObservableBoolean(false)
    val transactions: ObservableField<List<KinTransaction>> = ObservableField(ArrayList())
    val coupons: ObservableField<List<Coupon>> = ObservableField(ArrayList())

    private val type: Type = if (BuildConfig.DEBUG) Type.Test else Type.Main
    private val providerUrl = if (type == Type.Main) SDK_MAIN_NETWORK_URL else SDK_TEST_NETWORK_URL
    private val networkId = if (type == Type.Main) SDK_MAIN_NETWORK_ID else SDK_TEST_NETWORK_ID
    private val walletCacheName = if (type == Type.Test) TEST_NET_WALLET_CACHE_NAME else MAIN_NET_WALLET_CACHE_NAME

    init {
        walletCache = dataStoreProvider.dataStore(walletCacheName)
        kinClient = KinClient(context, Environment(providerUrl, networkId), KINIT_APP_ID)
        account = if (kinClient.hasAccount()) {
            kinClient.getAccount(kinClient.accountCount - 1)
        } else {
            kinClient.addAccount()
        }
        userRepo.userInfo.publicAddress = account.publicAddress!!
    }

    private fun getMigrationManager(): MigrationManager {
        val coreUrl = if (type == Type.Main) CORE_MAIN_NETWORK_URL else CORE_TEST_NETWORK_URL
        val coreId = if (type == Type.Main) CORE_MAIN_NETWORK_ID else CORE_TEST_NETWORK_ID
        val coreIssuer = if (type == Type.Main) CORE_ISSUER_MAIN else CORE_ISSUER_TEST
        val migrationUrl = (if (type == Type.Main) MIGRATE_ACCOUNT_SERVICE_PRODUCTION_URL else MIGRATE_ACCOUNT_SERVICE_TEST_URL) + "user_id=${userRepo.userId()}&public_address="
        val kinSdkVersion = IKinVersionProvider { KinSdkVersion.NEW_KIN_SDK }

        return MigrationManager(applicationContext,KINIT_APP_ID, MigrationNetworkInfo(coreUrl, coreId, providerUrl, networkId,coreIssuer,migrationUrl), kinSdkVersion, MigrationManagerListener())
    }

    private var isKin3: Boolean
        set(value) {
            walletCache.putBoolean(IS_KIN3, value)
            kin3Ready.set(value)
        }
        get() = walletCache.getBoolean(IS_KIN3, false)


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
    val kin3Ready: ObservableBoolean = ObservableBoolean(isKin3)
    val balance: ObservableField<String> = ObservableField(balanceInt.toString())

    fun updateBalance(callback: ResultCallback<Balance>? = null) {
        if (ready.get()) {
            account.balance.run(object : ResultCallback<Balance> {
                override fun onResult(currentBalance: Balance) {
                    balanceInt = currentBalance.value().toInt()
                    Log.d(TAG, "update balance  " + balance.get())
                    callback?.onResult(currentBalance)
                }

                override fun onError(exception: java.lang.Exception) {
                    Log.e(TAG, "no update balance")
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


    fun retrieveCoupons(callback: OperationCompletionCallback? = null) {
        walletApi.getCoupons(userRepo.userId()).enqueue(object : Callback<WalletApi.CouponsResponse> {
            override fun onResponse(call: Call<WalletApi.CouponsResponse>?,
                                    response: Response<WalletApi.CouponsResponse>?) {
                if (response != null && response.isSuccessful) {
                    Log.d(TAG, "onResponse: ${response.body()}")
                    val couponsList = response.body()
                    if (couponsList?.coupons != null && couponsList.coupons.isNotEmpty() && couponsList.status.equals(
                                    "ok")) {
                        coupons.set(couponsList.coupons)
                    } else {
                        Log.d(TAG, "coupons list empty or null ")
                        coupons.set(ArrayList())
                    }
                    callback?.onSuccess()
                } else {
                    Log.d(TAG, "onResponse null or isSuccessful=false: $response")
                    callback?.onError(ERROR_EMPTY_RESPONSE)
                }
            }

            override fun onFailure(call: Call<WalletApi.CouponsResponse>?, t: Throwable?) {
                Log.d(TAG, "onFailure called with throwable $t")
                callback?.onError(ERROR_APP_SERVER_FAILED_RESPONSE)
            }
        })
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
        } catch (e: OperationFailedException) {
            Log.e(TAG, "OperationFailedException occurred while retrieving balance ${e.message}")
            analytics.logEvent(Events.BILog.BalanceUpdateFailed(e.toString() + ":" + e.message))
        }
    }

    fun payForOrderSync(validationToken: String?, toAddress: String, amount: Int, orderId: String): TransactionId? {
        var transactionId: TransactionId? = null
        var paddr = account.publicAddress
        var transaction = account.buildTransactionSync(toAddress, BigDecimal(amount), 0, orderId).whitelistableTransaction
        val transactionInfo = WalletApi.TransactionInfo(orderId, paddr!!, toAddress, amount, transaction.transactionPayload, validationToken)
        var response = walletApi.addSignature(userRepo.userId(), transactionInfo).execute()
        if (response.isSuccessful && response.body()!=null) {
            transactionId = account.sendWhitelistTransactionSync(response.body()?.signedTransaction)
        }
        return transactionId
    }

    fun initKinWallet() {
        Log.d(TAG, "initializing Kin Wallet")
        scheduler.executeOnBackground {
            updateBalanceSync()
        }
    }

    fun exportAccountToStr(passphrase: String): String? {
        return try {
            account.export(passphrase)
        } catch (cryptoException: CryptoException) {
            null
        }
    }

    fun importBackedUpAccount(exportedStr: String, passphrase: String): KinAccount? {
        return try {
            kinClient.deleteAccount(0)
            kinClient.importAccount(exportedStr, passphrase)
            account = kinClient.getAccount(kinClient.accountCount - 1)
            return account
        } catch (cryptoException: CryptoException) {
            null
        } catch (createAccountException: CreateAccountException) {
            null
        }
    }

    fun restoreWallet(userId: String) {
        userRepo.isBackedup = true
        activeWallet = true
        userRepo.updateUserId(userId)
        userRepo.userInfo.publicAddress = account.publicAddress!!
        analytics.setUserId(userRepo.userId())
        analytics.logEvent(Events.Business.WalletRestored())
        retrieveTransactions()
        retrieveCoupons()
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
            } else {
                errorMessage = "response: $response, isSuccessful: ${response?.isSuccessful}"
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
            analytics.logEvent(Events.Business.WalletCreated())
            scheduler.post {
                activeWallet = true
                isKin3 = true
            }
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

    fun logP2pTransactionCompleted(price: Int, txHash: String, isApp2App: Boolean) {
        analytics.incrementUserProperty(Events.UserProperties.TRANSACTION_COUNT, 1)
        val type = if (isApp2App) TRANSACTION_TYPE_APP_TO_APP else TRANSACTION_TYPE_P2P
        analytics.logEvent(Events.Business.KINTransactionSucceeded(price, txHash, type))
    }

    fun logSpendTransactionCompleted(price: Int, txHash: String) {
        analytics.incrementUserProperty(Events.UserProperties.SPEND_COUNT, 1)
        analytics.incrementUserProperty(Events.UserProperties.TOTAL_KIN_SPENT,
                price.toLong())
        analytics.incrementUserProperty(Events.UserProperties.TRANSACTION_COUNT, 1)
        analytics.logEvent(Events.Business.KINTransactionSucceeded(price,
                txHash, TRANSACTION_TYPE_SPEND))
    }

    fun logEarnTransactionCompleted(price: Int, txHash: String) {
        analytics.incrementUserProperty(Events.UserProperties.EARN_COUNT, 1)
        analytics.incrementUserProperty(Events.UserProperties.TOTAL_KIN_EARNED,
                price.toLong())
        analytics.incrementUserProperty(Events.UserProperties.TRANSACTION_COUNT, 1)
        analytics.logEvent(Events.Business.KINTransactionSucceeded(price,
                txHash, TRANSACTION_TYPE_EARN))
    }

    fun listenToPayment(taskId: String, memo: String) {
        paymentListener = account.addPaymentListener {
            if (it?.memo().equals(memo)) {
                updateBalanceForPayment(taskId, it)
                paymentListener.remove()
            }
        }
        scheduler.scheduleOnMain({
            if (!onEarnTransactionCompleted.get()) {
                setTaskState(taskId, TaskState.TRANSACTION_ERROR)
                paymentListener.remove()
            }
        }, REWARD_TIMEOUT)
    }

    private fun updateBalanceForPayment(taskId: String, payment: PaymentInfo? = null) {
        updateBalance(object : ResultCallback<Balance> {
            override fun onResult(balance: Balance) {
                if (payment != null) {
                    onEarnTransactionCompleted.set(true)
                    setTaskState(taskId, TaskState.TRANSACTION_COMPLETED)
                    retrieveTransactions()
                    logEarnTransactionCompleted(payment.amount().toInt(), payment.hash().id())
                } else {
                    setTaskState(taskId, TaskState.TRANSACTION_ERROR)
                }
            }

            override fun onError(e: Exception) {
                setTaskState(taskId, TaskState.TRANSACTION_ERROR)
                if (payment != null) {
                    logEarnTransactionCompleted(payment.amount().toInt(), payment.hash().id())
                }
            }
        })
    }


    fun onTransactionMessageReceived(transactionComplete: Push.TransactionCompleteMessage) {
        if (!isValid(transactionComplete)) {
            transactionComplete.taskId?.let {
                setTaskState(it, TaskState.TRANSACTION_ERROR)
            }
            return
        }
        updateBalance(object : ResultCallback<Balance> {
            override fun onResult(balance: Balance) {
                if (transactionComplete.kin != null && transactionComplete.txHash != null) {
                    onEarnTransactionCompleted.set(true)
                    setTaskState(transactionComplete.taskId!!, TaskState.TRANSACTION_COMPLETED)
                    retrieveTransactions()
                    logEarnTransactionCompleted(transactionComplete.kin, transactionComplete.txHash)
                } else {
                    setTaskState(transactionComplete.taskId!!, TaskState.TRANSACTION_ERROR)
                }
            }

            override fun onError(e: Exception) {
                setTaskState(transactionComplete.taskId!!, TaskState.TRANSACTION_ERROR)
                if (transactionComplete.kin != null && transactionComplete.txHash != null) {
                    logEarnTransactionCompleted(transactionComplete.kin, transactionComplete.txHash)
                }
            }
        })

        Log.d(TAG, "KinMessagingService transactionComplete: $transactionComplete")
    }


    private fun isValid(transactionComplete: Push.TransactionCompleteMessage): Boolean {
        return transactionComplete.kin != null &&
                transactionComplete.kin > 0 &&
                transactionComplete.taskId != null &&
                transactionComplete.userId != null &&
                transactionComplete.userId == userRepo.userInfo.userId
    }

    private fun setTaskState(taskId: String, state: Int) {
        categoriesRepository.updateCurrentTaskState(taskId, state)
    }

    fun migrateWallet(migrationManagerCallbacks: IMigrationManagerCallbacks? = null) {
        val manager = getMigrationManager()
        if (!isKin3){
            manager.start(object : IMigrationManagerCallbacks {
                override fun onReady(newKinClient: IKinClient) {
                    Log.d("WalletMigration", "migrateWallet onReady for account: ${userRepo.userId()} " +
                            "with public address ${kinClient.getAccount(0)?.publicAddress}")
                    isKin3 = true
                    migrationManagerCallbacks?.onReady(null)
                }

                override fun onMigrationStart() {
                    Log.d("WalletMigration", "migrateWallet onMigrationStart for account: ${userRepo.userId()} " +
                            "with public address ${kinClient.getAccount(0)?.publicAddress}")
                    migrationManagerCallbacks?.onMigrationStart()
                }

                override fun onError(e: java.lang.Exception?) {
                    Log.d("WalletMigration", "migrateWallet onError for account: ${userRepo.userId()} " +
                            "with public address ${kinClient.getAccount(0)?.publicAddress} with error: $e")
                    migrationManagerCallbacks?.onError(e)
                }
            })
        } else
            migrationManagerCallbacks?.onReady(null)
    }

}
