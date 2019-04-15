package org.kinecosystem.kinit.viewmodel.bootwallet

import android.databinding.ObservableBoolean
import android.view.View

enum class BootAction {
    CREATE,
    MIGRATE
}

interface BootWalletViewModel {
    var listener: BootWalletEventsListener?
    var isError: ObservableBoolean
    var walletAction: BootAction
    fun onDestroy() {}
    fun bootWallet()
    fun onRetryClicked(view: View?)
    fun onContactSupportClicked(view: View?)
}