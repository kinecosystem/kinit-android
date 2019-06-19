package org.kinecosystem.kinit.view.onewallet

import org.kinecosystem.kinit.blockchain.TopupResult

interface TopupViewActions {

    fun displayInProgress()
    fun displaySuccess(topupResult: TopupResult)
    fun displayError(resId: Int)
    fun updateBalance(amount: Int)

}
