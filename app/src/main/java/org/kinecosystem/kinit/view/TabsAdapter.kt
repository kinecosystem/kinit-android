package org.kinecosystem.kinit.view

import android.databinding.DataBindingUtil
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.kinecosystem.kinit.CoreComponentsProvider
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.*
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.view.adapter.BalancePagerViewsAdapter
import org.kinecosystem.kinit.view.adapter.OfferListAdapter
import org.kinecosystem.kinit.view.customView.LockableViewPager
import org.kinecosystem.kinit.viewmodel.balance.BalanceViewModel
import org.kinecosystem.kinit.viewmodel.earn.EarnViewModel
import org.kinecosystem.kinit.viewmodel.info.InfoViewModel
import org.kinecosystem.kinit.viewmodel.spend.SpendViewModel

private const val NUMBER_OF_TABS = 5
private const val PRE_EARN_SHOW_DURATION: Long = 2000L

class TabsAdapter(private val coreComponents: CoreComponentsProvider) : PagerAdapter() {
    private var models = arrayOfNulls<TabViewModel?>(NUMBER_OF_TABS)
    private var positionToBeViewed: Int? = null
    private var preAnimationWasShown = false

    companion object {
        const val PRE_EARN_TAB_INDEX = 0
        const val EARN_TAB_INDEX = 1
        const val SPEND_TAB = 2
        const val BALANCE_TAB = 3
        const val INFO_TAB = 4
    }

    override fun instantiateItem(parent: ViewGroup, position: Int): View {
        val tabView = when (position) {
            PRE_EARN_TAB_INDEX -> getPreEarnTab(parent, position)
            EARN_TAB_INDEX -> getEarnTab(parent, position)
            SPEND_TAB -> getSpendTab(parent, position)
            BALANCE_TAB -> getBalanceTab(parent, position)
            INFO_TAB -> getInfoTab(parent, position)
            else -> getInfoTab(parent, position)
        }
        parent.addView(tabView)
        if (position == positionToBeViewed) {
            onTabVisibleToUser(position)
            positionToBeViewed = null
        }
        return tabView
    }

    fun shouldShowAnimation(): Boolean {
        return if (preAnimationWasShown) false
        else coreComponents.questionnaireRepo().isQuestionnaireAvaliable()
    }

    override fun getCount(): Int {
        return NUMBER_OF_TABS
    }

    override fun destroyItem(parent: ViewGroup, position: Int, view: Any) {
        parent.removeView(view as View)
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return ""
    }

    private fun getPreEarnTab(parent: ViewGroup, position: Int): View {
        val binding = DataBindingUtil.inflate<PreEarnTabBinding>(LayoutInflater.from(parent.context),
            R.layout.pre_earn_tab, parent, false)
        binding.model = PreEarnViewModel(coreComponents)
        models[position] = binding.model
        moveToEarnTab(parent, position, binding)
        return binding.root
    }

    private fun moveToEarnTab(parent: ViewGroup, position: Int, binding: PreEarnTabBinding) {
        if (!preAnimationWasShown && coreComponents.questionnaireRepo().isQuestionnaireAvaliable()) {
            parent.postDelayed({
                if (parent is LockableViewPager) {
                    // move to Eran Tab
                    parent.setCurrentItem(EARN_TAB_INDEX, true)
                    preAnimationWasShown = true
                }
            }, PRE_EARN_SHOW_DURATION)
        }
    }

    private fun getEarnTab(parent: ViewGroup, position: Int): View {
        val binding = DataBindingUtil.inflate<EarnTabBinding>(LayoutInflater.from(parent.context),
            R.layout.earn_tab, parent, false)
        binding.model = EarnViewModel(coreComponents, Navigator(parent.context))
        models[position] = binding.model
        return binding.root
    }

    private fun getSpendTab(parent: ViewGroup, position: Int): View {
        val binding = DataBindingUtil.inflate<SpendTabBinding>(LayoutInflater.from(parent.context),
            R.layout.spend_tab, parent, false)
        binding.model = SpendViewModel(coreComponents, Navigator(parent.context))
        binding.offersList.layoutManager = LinearLayoutManager(parent.context)
        binding.offersList.adapter = OfferListAdapter(parent.context, binding.model)
        models[position] = binding.model
        return binding.root
    }

    private fun getBalanceTab(parent: ViewGroup, position: Int): View {
        val binding = DataBindingUtil.inflate<BalanceTabBinding>(LayoutInflater.from(parent.context),
            R.layout.balance_tab, parent, false)
        binding.model = BalanceViewModel(coreComponents)
        binding.tabsContent.adapter = BalancePagerViewsAdapter(parent.context, coreComponents, binding)
        binding.balanceNavTabs.setupWithViewPager(binding.tabsContent)
        models[position] = binding.model
        return binding.root
    }

    private fun getInfoTab(parent: ViewGroup, position: Int): View {
        val binding = DataBindingUtil.inflate<InfoTabBinding>(LayoutInflater.from(parent.context),
            R.layout.info_tab, parent, false)
        binding.model = InfoViewModel(coreComponents)
        models[position] = binding.model
        return binding.root
    }

    fun onTabVisibleToUser(position: Int) {
        if (models[position] == null) {
            positionToBeViewed = position
        } else {
            models[position]?.onScreenVisibleToUser()
        }
    }
}


class PreEarnViewModel(coreComponents: CoreComponentsProvider) :
    TabViewModel {
    override fun onScreenVisibleToUser() {

    }
}