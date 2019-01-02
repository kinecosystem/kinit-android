package org.kinecosystem.tippic.view.comingSoon

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.view.SingleFragmentActivity
import org.kinecosystem.tippic.viewmodel.EcoAppsComingSoonViewModel

class EcoAppsComingSoonActivity : SingleFragmentActivity(), EcoAppsComingSoonViewModel.ComingSoonActions {
    companion object {
        fun getIntent(context: Context) = Intent(context, EcoAppsComingSoonActivity::class.java)
    }

    private var webfragment: EcoAppsComingSoonWebFragment? = null
    private var model: EcoAppsComingSoonViewModel = EcoAppsComingSoonViewModel()

    override fun getFragment(): Fragment {
        return EcoAppsComingSoonWebFragment.getInstance()
    }

    init {
        TippicApplication.coreComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model.listener = this
    }

    override fun onDestroy() {
        model.listener = null
        super.onDestroy()
    }

    fun getModel(): EcoAppsComingSoonViewModel {
        return model
    }

    override fun onClose() {
        finish()
    }
}