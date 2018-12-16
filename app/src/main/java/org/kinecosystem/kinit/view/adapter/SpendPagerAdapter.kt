package org.kinecosystem.kinit.view.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.EcoappsTransfersLayoutBinding
import org.kinecosystem.kinit.databinding.OffersLayoutBinding
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.view.TabViewModel
import org.kinecosystem.kinit.viewmodel.spend.EcoAppsViewModel
import org.kinecosystem.kinit.viewmodel.spend.OffersViewModel


private const val NUMBER_OF_TABS = 2

class SpendPagerAdapter(val context: Context, private val tabIndexListener: OnTabIndexListener) : PagerAdapter(), TabViewModel, ViewPager.OnPageChangeListener {
    val appsModel = EcoAppsViewModel()
    val offersModel = OffersViewModel(Navigator(context))

    override fun onScreenVisibleToUser() {
        when (tabIndexListener.currentTab()) {
            0 -> appsModel.onScreenVisibleToUser()
            else -> offersModel.onScreenVisibleToUser()
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        onTabVisible(position)
    }

    override fun getCount(): Int {
        return NUMBER_OF_TABS
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> context.resources.getString(R.string.ecosystem_apps)
            else -> context.resources.getString(R.string.gift_cards)
        }
    }

    private fun onTabVisible(position: Int) {
        when (position) {
            0 -> appsModel.onScreenVisibleToUser()
            else -> offersModel.onScreenVisibleToUser()
        }
    }

    private fun buildAppsGroupView(parent: ViewGroup): ViewGroup {
        val ecoAppsBinding = DataBindingUtil.inflate<EcoappsTransfersLayoutBinding>(LayoutInflater.from(context), R.layout.ecoapps_transfers_layout, parent, false)
        ecoAppsBinding.model = appsModel
        ecoAppsBinding.scrollview.isFocusableInTouchMode = true
        ecoAppsBinding.scrollview.descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
        return ecoAppsBinding.mainLayout
    }

    private fun buildOffersGroupView(parent: ViewGroup): ViewGroup {
        val offersBinding = DataBindingUtil.inflate<OffersLayoutBinding>(LayoutInflater.from(context), R.layout.offers_layout, parent, false)
        offersBinding.model = offersModel
        offersBinding.offersList.layoutManager = LinearLayoutManager(context)
        offersBinding.offersList.adapter = OfferListAdapter(context, offersModel)
        return offersBinding.mainLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): ViewGroup {
        val view = when (position) {
            0 -> buildAppsGroupView(container)
            else -> buildOffersGroupView(container)
        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }


    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }
}

interface OnTabIndexListener {
    fun currentTab(): Int
}
