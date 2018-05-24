package org.kinecosystem.kinit.viewmodel.balance

import org.kinecosystem.kinit.model.KinTransaction
import org.kinecosystem.kinit.util.TimeUtils
import org.kinecosystem.kinit.view.adapter.TransactionsListAdapter

class TransactionViewModel(val transaction: KinTransaction, viewType: Int?, transactionsCount : Int) {

    private val txTimeInSeconds = transaction.date ?: 0

    val isMoreThanADay: Boolean = TimeUtils.diffMoreThanADay(txTimeInSeconds)
    val formatHHMM: String = TimeUtils.secondsToHHMM(txTimeInSeconds)
    val formatDayMonthShort: String = TimeUtils.secondsToDayMMM(txTimeInSeconds)

    val clientReceived = transaction.clientReceived ?: true
    val amount =
        if (clientReceived) "+${transaction.amount} K"
        else "-${transaction.amount} K"

    val transactionProviderIconUrl = transaction.provider?.imageUrl ?: ""
    val isTopLineVisible = when (viewType) {
        TransactionsListAdapter.OTHER_TRANSACTION -> true
        TransactionsListAdapter.BOTTOM_TRANSACTION -> false
        TransactionsListAdapter.TOP_TRANSACTION -> transactionsCount > 1
        else -> true
    }
    val isBottomLineVisible = when (viewType) {
        TransactionsListAdapter.OTHER_TRANSACTION -> true
        TransactionsListAdapter.BOTTOM_TRANSACTION -> transactionsCount > 1
        TransactionsListAdapter.TOP_TRANSACTION -> false
        else -> true
    }
}