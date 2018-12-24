package org.kinecosystem.kinit.view.comingSoon

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.view.SingleFragmentActivity
import org.kinecosystem.kinit.viewmodel.EcoAppsComingSoonViewModel

class EcoAppsComingSoonActivity : SingleFragmentActivity(), EcoAppsComingSoonViewModel.ComingSoonActions {
    companion object {
        fun getIntent(context: Context) = Intent(context, EcoAppsComingSoonActivity::class.java)
    }

    private var webfragment: EcoAppsComingSoonWebFragment? = null
    private var model: EcoAppsComingSoonViewModel = EcoAppsComingSoonViewModel()

    override fun getFragment(): Fragment {
        webfragment = EcoAppsComingSoonWebFragment.getInstance()
        return webfragment as EcoAppsComingSoonWebFragment
    }

    init {
        KinitApplication.coreComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model.listener = this
    }

    fun getModel(): EcoAppsComingSoonViewModel {
        return model
    }

    override fun onClose() {
        finish()
    }
}