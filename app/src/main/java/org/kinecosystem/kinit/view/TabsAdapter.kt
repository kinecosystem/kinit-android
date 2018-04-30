package org.kinecosystem.kinit.view

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

private const val NUMBER_OF_TABS = 4

class TabsAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> EarnFragment.newInstance(0)
            1 -> SpendFragment.newInstance(1)
            2 -> BalanceFragment.newInstance(2)
            3 -> InfoFragment.newInstance(3)
            else -> InfoFragment.newInstance(3)
        }
    }

    override fun getCount(): Int {
        return NUMBER_OF_TABS
    }

}
