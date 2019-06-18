package org.kinecosystem.kinit.view.spend

interface PurchaseOfferActions {

    fun animateBuy()

    fun updateBuyButtonWidth()

    fun closeScreen()

    fun showDialog(resTitle: Int, resMessage: Int, resAction: Int, shouldFinish: Boolean, logErrorType: String? = null)

    fun shareCode(code: String)

}