package org.kinecosystem.kinit.view

import android.databinding.DataBindingUtil
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.databinding.*
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.network.TaskService
import org.kinecosystem.kinit.network.Wallet
import org.kinecosystem.kinit.repository.TasksRepository
import org.kinecosystem.kinit.util.Scheduler
import org.kinecosystem.kinit.view.BottomTabNavigation.PageSelectionListener
import org.kinecosystem.kinit.view.adapter.BalancePagerViewsAdapter
import org.kinecosystem.kinit.view.adapter.OfferListAdapter
import org.kinecosystem.kinit.view.customView.LockableViewPager
import org.kinecosystem.kinit.viewmodel.balance.BalanceViewModel
import org.kinecosystem.kinit.viewmodel.earn.EarnViewModel
import org.kinecosystem.kinit.viewmodel.info.InfoViewModel
import org.kinecosystem.kinit.viewmodel.spend.SpendViewModel
import javax.inject.Inject

private const val NUMBER_OF_TABS = 5
private const val PRE_EARN_SHOW_DURATION: Long = 2000L

class TabsAdapter :
    PagerAdapter() {

    private var models = arrayOfNulls<TabViewModel?>(NUMBER_OF_TABS)
    private var positionToBeViewed: Int? = null
    private var preAnimationWasShown = false
    private var pageSelectionListener: PageSelectionListener? = null

    companion object {
        const val PRE_EARN_TAB_INDEX = 0
        const val EARN_TAB_INDEX = 1
        const val SPEND_TAB = 2
        const val BALANCE_TAB = 3
        const val INFO_TAB = 4
    }

    @Inject
    lateinit var analytics: Analytics
    @Inject
    lateinit var tasksRepository: TasksRepository
    @Inject
    lateinit var scheduler: Scheduler
    @Inject
    lateinit var wallet: Wallet
    @Inject
    lateinit var taskService: TaskService

    init {
        KinitApplication.coreComponent.inject(this)
    }

    fun setPageSelectionListener(pageSelectionListener: PageSelectionListener) {
        this.pageSelectionListener = pageSelectionListener
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
        else tasksRepository.isTaskAvailable()
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
        binding.model = PreEarnViewModel()
        models[position] = binding.model
        moveToEarnTab(parent)
        return binding.root
    }

    private fun moveToEarnTab(parent: ViewGroup) {
        if (!preAnimationWasShown && tasksRepository.isTaskAvailable()) {
            parent.postDelayed({
                if (parent is LockableViewPager) {
                    // move to Eran Tab
                    parent.setCurrentItem(EARN_TAB_INDEX, true, LockableViewPager.TransitionCompletedListener { pageSelectionListener?.enablePageSelection() })
                    preAnimationWasShown = true
                }
            }, PRE_EARN_SHOW_DURATION)
        }
    }

    private fun getEarnTab(parent: ViewGroup, position: Int): View {
        val binding = DataBindingUtil.inflate<EarnTabBinding>(LayoutInflater.from(parent.context),
            R.layout.earn_tab, parent, false)
        binding.model = EarnViewModel(tasksRepository, wallet, taskService, scheduler, analytics,
            Navigator(parent.context))
        models[position] = binding.model
        return binding.root
    }

    private fun getSpendTab(parent: ViewGroup, position: Int): View {
        val binding = DataBindingUtil.inflate<SpendTabBinding>(LayoutInflater.from(parent.context),
            R.layout.spend_tab, parent, false)
        val spendingModel = SpendViewModel(Navigator(parent.context))
        binding.model = spendingModel
        binding.offersList.layoutManager = LinearLayoutManager(parent.context)
        binding.offersList.adapter = OfferListAdapter(parent.context, spendingModel)
        models[position] = binding.model
        return binding.root
    }

    private fun getBalanceTab(parent: ViewGroup, position: Int): View {
        val binding = DataBindingUtil.inflate<BalanceTabBinding>(LayoutInflater.from(parent.context),
            R.layout.balance_tab, parent, false)
        binding.model = BalanceViewModel()
        binding.tabsContent.adapter = BalancePagerViewsAdapter(parent.context, binding)
        binding.balanceNavTabs.setupWithViewPager(binding.tabsContent)
        models[position] = binding.model
        return binding.root
    }

    private fun getInfoTab(parent: ViewGroup, position: Int): View {
        val binding = DataBindingUtil.inflate<InfoTabBinding>(LayoutInflater.from(parent.context),
            R.layout.info_tab, parent, false)
        binding.model = InfoViewModel()
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


class PreEarnViewModel : TabViewModel {

    override fun onScreenVisibleToUser() {

    }
}