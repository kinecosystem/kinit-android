package org.kinecosystem.kinit.view.earn

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.view.View
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.repository.TasksRepository
import org.kinecosystem.kinit.view.tutorial.BaseSingleFragmentActivity
import javax.inject.Inject


class WebTaskActivity : BaseSingleFragmentActivity() {
    override fun init() {
        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, WebTaskActivity::class.java)
    }

    @Inject
    lateinit var tasksRepository: TasksRepository

    override fun inject() {
        KinitApplication.coreComponent.inject(this)
    }

    override fun getFragment(): Fragment {
        return when (tasksRepository.task!!.type) {
            "truex" -> WebTaskTruexFragment.getInstance()
            else -> WebTaskTruexFragment.getInstance()
        }
    }
}