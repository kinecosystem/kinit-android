package org.kinecosystem.kinit.view

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.databinding.ObservableBoolean
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.blockchain.Wallet
import org.kinecosystem.kinit.databinding.BalanceTabBinding
import org.kinecosystem.kinit.databinding.EarnCategoriesTabBinding
import org.kinecosystem.kinit.databinding.InfoTabBinding
import org.kinecosystem.kinit.databinding.SpendTabBinding
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.server.TaskService
import org.kinecosystem.kinit.util.Scheduler
import org.kinecosystem.kinit.view.adapter.BalancePagerViewsAdapter
import org.kinecosystem.kinit.view.adapter.CategoryListAdapter
import org.kinecosystem.kinit.view.adapter.OfferListAdapter
import org.kinecosystem.kinit.view.customView.AlertManager
import org.kinecosystem.kinit.viewmodel.balance.BalanceViewModel
import org.kinecosystem.kinit.viewmodel.earn.CategoriesViewModel
import org.kinecosystem.kinit.viewmodel.info.InfoViewModel
import org.kinecosystem.kinit.viewmodel.spend.SpendViewModel
import javax.inject.Inject


class TabsAdapter(val context: Context) :
        PagerAdapter() {

    private var models = arrayOfNulls<TabViewModel?>(NUMBER_OF_TABS)
    private var positionToBeViewed: Int? = null

    companion object {
        const val EARN_TAB_INDEX = 0
        const val SPEND_TAB = 1
        const val BALANCE_TAB = 2
        const val INFO_TAB = 3
        private const val NUMBER_OF_TABS = 4
    }

    @Inject
    lateinit var analytics: Analytics
    @Inject
    lateinit var scheduler: Scheduler
    @Inject
    lateinit var wallet: Wallet
    @Inject
    lateinit var taskService: TaskService

    init {
        KinitApplication.coreComponent.inject(this)
    }

    override fun instantiateItem(parent: ViewGroup, position: Int): View {
        val tabView = when (position) {
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

    private fun getEarnTab(parent: ViewGroup, position: Int): View {
        val binding = DataBindingUtil.inflate<EarnCategoriesTabBinding>(LayoutInflater.from(context),
                R.layout.earn_categories_tab, parent, false)
        val model = CategoriesViewModel(Navigator(context))
        binding.categories.layoutManager = GridLayoutManager(context, 2)
        binding.categories.adapter = CategoryListAdapter(context, model)
        binding.model = model
        models[position] = model
        return binding.root
    }

    private fun getSpendTab(parent: ViewGroup, position: Int): View {
        val binding = DataBindingUtil.inflate<SpendTabBinding>(LayoutInflater.from(context),
                R.layout.spend_tab, parent, false)
        val spendingModel = SpendViewModel(Navigator(context))
        binding.model = spendingModel
        binding.offersList.layoutManager = LinearLayoutManager(context)
        binding.offersList.adapter = OfferListAdapter(context, spendingModel)
        models[position] = binding.model
        return binding.root
    }

    private fun getBalanceTab(parent: ViewGroup, position: Int): View {
        val binding = DataBindingUtil.inflate<BalanceTabBinding>(LayoutInflater.from(context),
                R.layout.balance_tab, parent, false)
        binding.model = BalanceViewModel()
        binding.tabsContent.adapter = BalancePagerViewsAdapter(context, binding)
        binding.balanceNavTabs.setupWithViewPager(binding.tabsContent)
        models[position] = binding.model
        return binding.root
    }

    private fun getInfoTab(parent: ViewGroup, position: Int): View {
        val binding = DataBindingUtil.inflate<InfoTabBinding>(LayoutInflater.from(context),
                R.layout.info_tab, parent, false)
        val navigator = Navigator(context)
        var model = InfoViewModel(navigator)
        model.showCreateNewBackupAlert.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (sender is ObservableBoolean && sender.get()) {
                    AlertManager.showAlert(context, R.string.rebackup_title, R.string.reback_message, R.string.new_backup, {
                        navigator.navigateTo(Navigator.Destination.WALLET_BACKUP)
                    }, R.string.cancel, {})
                    model.onShowingCreateNewBackupAlert()
                }
            }
        })
        binding.model = model
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