package org.kinecosystem.kinit.view.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.SpendTabBinding

private const val NUMBER_OF_TABS = 2

class SpendPagerViewsAdapter(val context: Context, val binding: SpendTabBinding) : PagerAdapter() {

    init {
        binding.offersList.layoutManager = LinearLayoutManager(context)
        binding.model?.let {
            val adapter = OfferListAdapter(context, it)
            binding.offersList.adapter = adapter

        }

        //binding.transactionsRecycleView.adapter = TransactionsListAdapter(context)
        // binding.couponsRecycleView.adapter = CouponsListAdapter(context)
    }

    private fun getPage(position: Int): ViewGroup {
        return when (position) {
            0 -> binding.spendPageWrapper
            else -> binding.transferPageWrapper
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): ViewGroup {
        val view: ViewGroup = getPage(position)
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }

    override fun getCount(): Int {
        return NUMBER_OF_TABS
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> context.resources.getString(R.string.spend_kin)
            else -> context.resources.getString(R.string.transfer_kin)
        }
    }

    fun onDestroyView() {
        //binding.transactionsRecycleView.adapter.onDetachedFromRecyclerView(binding.transactionsRecycleView)
        //binding.couponsRecycleView.adapter.onDetachedFromRecyclerView(binding.couponsRecycleView)
    }
}
