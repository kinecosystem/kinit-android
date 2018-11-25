package org.kinecosystem.kinit.view.spend

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.EcoappsTransfersLayoutBinding
import org.kinecosystem.kinit.view.BaseFragment
import org.kinecosystem.kinit.viewmodel.spend.EcoAppsViewModel

class EcoAppsTabFragment : BaseFragment() {

    private var model: EcoAppsViewModel? = null
    lateinit var binding: EcoappsTransfersLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KinitApplication.coreComponent.inject(this)
        context?.let {
            model = EcoAppsViewModel()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.ecoapps_transfers_layout, container, false)
        binding.model = model
        binding.scrollview.isFocusableInTouchMode = true
        binding.scrollview.descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
        return binding.root
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            model?.onScreenVisibleToUser()
            Log.d("####", "#### screen eco visibile")
        }
    }

    companion object {
        val TAG = EcoAppsTabFragment::class.java.simpleName

        fun newInstance(): EcoAppsTabFragment {
            return EcoAppsTabFragment()
        }
    }
}
