package org.kinecosystem.kinit.view.spend

import android.content.Context
import android.content.Intent
import android.os.Bundle
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.model.spend.EcosystemApp
import org.kinecosystem.kinit.view.BaseActivity

private const val APP_PARAM = "AppInfoActivity_APP_PARAM"

class AppDetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.single_fragment_layout)
        val app = intent.getParcelableExtra<EcosystemApp>(APP_PARAM)
        val fragment = AppDetailFragment.newInstance(app)
        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment).commit()
    }

    companion object {
        fun getIntent(context: Context, app: EcosystemApp): Intent {
            val intent = Intent(context, AppDetailsActivity::class.java)
            intent.putExtra(APP_PARAM, app)
            return intent
        }
    }

}
