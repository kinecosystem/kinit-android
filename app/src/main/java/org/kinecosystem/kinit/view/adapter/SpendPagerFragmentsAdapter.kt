package org.kinecosystem.kinit.view.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.view.spend.OffersTabFragment
import org.kinecosystem.kinit.view.spend.EcoAppsTabFragment

private const val NUMBER_OF_TABS = 2

class SpendPagerFragmentsAdapter(val context: Context, supportFragmentManager: FragmentManager) : FragmentPagerAdapter(supportFragmentManager) {
    override fun getItem(position: Int): Fragment {
        return if (position == 0)  EcoAppsTabFragment.newInstance()
        else OffersTabFragment.newInstance()
    }

    override fun getCount(): Int {
        return NUMBER_OF_TABS
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 ->  context.resources.getString(R.string.transfer_kin)
            else -> context.resources.getString(R.string.spend_kin)
        }
    }

}
