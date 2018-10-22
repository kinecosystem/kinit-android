package org.kinecosystem.kinit.view.earn

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.view.View
import org.kinecosystem.kinit.view.tutorial.BaseSingleFragmentActivity


class WebTaskActivity : BaseSingleFragmentActivity() {

    override fun inject() {
    }

    override fun init() {
        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, WebTaskActivity::class.java)
    }

    override fun getFragment(): Fragment {
        return WebTaskTruexFragment.getInstance()
    }
}