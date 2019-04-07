package org.kinecosystem.kinit.viewmodel.bootwallet

import android.view.View

enum class BootAction {
    CREATE,
    MIGRATE
}

interface BootWalletActions {
    var listener: BootWalletEventsListener?
    var walletAction: BootAction
    fun onDestroy() {}
    fun bootWallet()
    fun onRetryClicked(view: View?)
    fun onContactSupportClicked(view: View?)
}