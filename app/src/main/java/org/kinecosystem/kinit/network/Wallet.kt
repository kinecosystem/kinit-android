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
import org.kinecosystem.kinit.analytics.Analytics.MENU_ITEM_NAME_SPEND
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.Push
import org.kinecosystem.kinit.model.TaskState
import org.kinecosystem.kinit.repository.DataStore
import org.kinecosystem.kinit.repository.DataStoreProvider
import org.kinecosystem.kinit.repository.QuestionnaireRepository
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.util.Scheduler
import java.math.BigDecimal

private const val TEST_NET_URL = "https://horizon-testnet.stellar.org"
private const val MAIN_NET_URL = "https://horizon.stellar.org"
private const val TEST_NET_WALLET_CACHE_NAME = "kin.app.wallet.testnet"
private const val MAIN_NET_WALLET_CACHE_NAME = "kin.app.wallet.mainnet"
private const val ACTIVE_WALLET_KEY = "activeWallet"
private const val WALLET_BALANCE_KEY = "WalletBalance"

class Wallet(context: Context, dataStoreProvider: DataStoreProvider,
    val userRepo: UserRepository,
    val questionnaireRepo: QuestionnaireRepository,
    val analytics: Analytics,
    val onboardingApi: OnboardingApi,
    val scheduler: Scheduler,
    type: Type = Type.Test) {

    enum class Type {
        Main,
        Test
    }

    val onEarnTransactionCompleted: ObservableBoolean = ObservableBoolean(false)

    private val walletCache: DataStore
    private var kinClient: KinClient
    private var account: KinAccount

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
    val ready: ObservableBoolean = ObservableBoolean(activeWallet)

    var balanceInt: Int
        private set(value) {
            walletCache.putInt(WALLET_BALANCE_KEY, value)
            balance.set(value.toString())
            logBalanceUpdated(value.toDouble())
        }
        get() = walletCache.getInt(WALLET_BALANCE_KEY, 0)

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

    fun logSpendTransactionCompleted(price: Int, orderId: String) {
        analytics.incrementUserProperty(Events.UserProperties.SPEND_COUNT, 1)
        analytics.incrementUserProperty(Events.UserProperties.TOTAL_KIN_SPENT,
            price.toLong())
        analytics.incrementUserProperty(Events.UserProperties.TRANSACTION_COUNT, 1)
        analytics.logEvent(Events.Business.KINTransactionSucceeded(price.toFloat(),
            orderId, MENU_ITEM_NAME_SPEND))
    }

    fun logEarnTransactionCompleted(price: Int, orderId: String) {
        analytics.incrementUserProperty(Events.UserProperties.EARN_COUNT, 1)
        analytics.incrementUserProperty(Events.UserProperties.TOTAL_KIN_EARNED,
            price.toLong())
        analytics.incrementUserProperty(Events.UserProperties.TRANSACTION_COUNT, 1)
        analytics.logEvent(Events.Business.KINTransactionSucceeded(price.toFloat(),
            orderId, Analytics.MENU_ITEM_NAME_EARN))
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
        questionnaireRepo.taskState = state
    }

}