package org.kinecosystem.kinit.view


import android.support.v4.app.Fragment
import org.kinecosystem.kinit.CoreComponentsProvider

const val TAB_INDEX = "TAB_INDEX"

open class BaseFragment : Fragment() {

    val coreComponents: CoreComponentsProvider
        get() = activity?.applicationContext as CoreComponentsProvider

    fun getTabIndex(): Int {
        return arguments?.getInt(TAB_INDEX, -1) ?: -1
    }
}