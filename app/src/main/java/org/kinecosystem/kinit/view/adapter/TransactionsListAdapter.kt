package org.kinecosystem.kinit.view.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import org.kinecosystem.kinit.dagger.CoreComponent
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.TransactionRowLayoutBinding
import org.kinecosystem.kinit.model.KinTransaction
import org.kinecosystem.kinit.network.ServicesProvider
import org.kinecosystem.kinit.viewmodel.balance.TransactionViewModel
import javax.inject.Inject

class TransactionsListAdapter(val context: Context)
    : RecyclerView.Adapter<TransactionsListAdapter.ViewHolder>() {

    @Inject
    lateinit var servicesProvider: ServicesProvider

    var transactions: ArrayList<KinTransaction>
    private val updateTransactionsCallback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            transactions = servicesProvider.walletService.transactions.get()
            notifyDataSetChanged()
        }
    }

    companion object {
        const val TOP_TRANSACTION = 0
        const val BOTTOM_TRANSACTION = 1
        const val OTHER_TRANSACTION = -1
    }

    init {
        KinitApplication.coreComponent.inject(this)
        transactions = servicesProvider.walletService.transactions.get()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        Log.d("####", "TransactionsListAdapter onAttachedToRecyclerView")
        servicesProvider.walletService.transactions.addOnPropertyChangedCallback(updateTransactionsCallback)
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        servicesProvider.walletService.transactions.removeOnPropertyChangedCallback(updateTransactionsCallback)
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsListAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding: TransactionRowLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.transaction_row_layout,
            parent, false)

        return when (viewType) {
            TOP_TRANSACTION -> TopTransactionViewHolder(binding)
            BOTTOM_TRANSACTION -> BottomTransactionViewHolder(binding)
            OTHER_TRANSACTION -> OtherTransactionViewHolder(binding)
            else -> OtherTransactionViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TOP_TRANSACTION
            itemCount - 1 -> BOTTOM_TRANSACTION
            else -> OTHER_TRANSACTION
        }
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tx: KinTransaction = transactions[position]
        holder.bind(tx, getItemViewType(position), itemCount)
    }

    abstract class ViewHolder(val binding: TransactionRowLayoutBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: KinTransaction, viewType: Int, transactionsCount: Int) {
            val model = TransactionViewModel(transaction, viewType, transactionsCount)
            binding.model = model
            binding.executePendingBindings()
        }
    }

    // Separate view type to force RecyclerView to *not* swap those items with others from other types
    class TopTransactionViewHolder(binding: TransactionRowLayoutBinding)
        : ViewHolder(binding)

    class BottomTransactionViewHolder(binding: TransactionRowLayoutBinding)
        : ViewHolder(binding)

    class OtherTransactionViewHolder(binding: TransactionRowLayoutBinding)
        : ViewHolder(binding)
}
