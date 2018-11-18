package org.kinecosystem.kinit.view.spend

import org.kinecosystem.ClientValidator

interface PurchaseOfferActions {

    fun animateBuy()

    fun updateBuyButtonWidth()

    fun closeScreen()

    fun showDialog(resTitle: Int, resMessage: Int, resAction: Int, shouldFinish: Boolean, logErrorType: String? = null)

    fun shareCode(code: String)

    fun getClientValidator(): ClientValidator
}