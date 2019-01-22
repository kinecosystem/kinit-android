package org.kinecosystem.kinit.view.spend

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.view.SingleFragmentActivity
import org.kinecosystem.kinit.viewmodel.WebInfoViewModel

class EcoAppsInfoActivity : SingleFragmentActivity(), WebInfoViewModel.ComingSoonActions {

    companion object {
        private val EXTRA_TYPE = "type"

        fun getIntent(context: Context, type:WebInfoViewModel.Type):Intent {
            val intent = Intent(context, EcoAppsInfoActivity::class.java)
            intent.putExtra(EXTRA_TYPE, type)
            return intent
        }
    }

    private lateinit var model: WebInfoViewModel

    override fun getFragment(): Fragment {
        return EcoAppsInfoWebFragment.getInstance()
    }

    init {
        KinitApplication.coreComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val type = intent.getSerializableExtra(EXTRA_TYPE) as WebInfoViewModel.Type
        model = WebInfoViewModel(type)
        model.listener = this
    }

    override fun onDestroy() {
        model.listener = null
        super.onDestroy()
    }

    fun getModel(): WebInfoViewModel {
        return model
    }

    override fun onClose() {
        finish()
    }
}