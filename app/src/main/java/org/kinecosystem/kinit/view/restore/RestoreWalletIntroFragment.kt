package org.kinecosystem.kinit.view.restore

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.intro_fragment.*
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.view.BaseFragment
import org.kinecosystem.kinit.viewmodel.bootwallet.RestoreWalletViewModel

class RestoreWalletIntroFragment : BaseFragment() {

    lateinit var model: RestoreWalletViewModel
    lateinit var actions: RestoreWalletActions

    companion object {
        val TAG = RestoreWalletIntroFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(): RestoreWalletIntroFragment {
            return RestoreWalletIntroFragment()
        }
    }

    init {
        KinitApplication.coreComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        try {
            actions = activity as RestoreWalletActions
            model = (activity as RestoreWalletActivity).getModel()
        } catch (e: Exception) {
            Log.e(TAG, "activity must be RestoreWalletActivity and implement RestoreWalletActions")
            activity?.finish()
        }
        return inflater.inflate(R.layout.intro_fragment, container, false)
    }

    override fun onResume() {
        super.onResume()
        actions.getModel().onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity !is RestoreWalletActions) {
            Log.e(TAG, "Activity must implement RestoreActions")
            activity?.finish()
        }
        backBtn.setOnClickListener { actions.moveBack() }
        startBtn.setOnClickListener { moveToScanner() }
    }

    private fun moveToScanner() {
        activity?.let {
            if (ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                actions.getModel().analytics.logEvent(Events.Analytics.ClickScanButtonOn2StepsAwayPage())
                actions.moveToNextScreen()
            } else {
                actions.getCameraPermissions()
            }
        }
    }
}