package org.kinecosystem.kinit.view.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import org.kinecosystem.kinit.CoreComponentsProvider
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.BalanceFragmentBinding

private const val NUMBER_OF_TABS = 2

class BalancePagerViewsAdapter(val context: Context, val coreComponents: CoreComponentsProvider, val binding: BalanceFragmentBinding) : PagerAdapter() {

    init {
        binding.transactionsRecycleView.adapter = TransactionsListAdapter(context, coreComponents)
        binding.couponsRecycleView.adapter = CouponsListAdapter(context, coreComponents)
    }

    private fun getPage(position: Int): ViewGroup {
        return when (position) {
            0 -> binding.transactionsPageWrapper
            1 -> binding.couponsPageWrapper
            else -> binding.transactionsPageWrapper
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
            0 -> context.resources.getString(R.string.recent_activity_tab_text)
            1 -> context.resources.getString(R.string.my_vouchers_tab_text)
            else -> context.resources.getString(R.string.recent_activity_tab_text)
        }
    }

    fun onDestroyView() {
        binding.transactionsRecycleView.adapter.onDetachedFromRecyclerView(binding.transactionsRecycleView)
        binding.couponsRecycleView.adapter.onDetachedFromRecyclerView(binding.couponsRecycleView)
    }
}
