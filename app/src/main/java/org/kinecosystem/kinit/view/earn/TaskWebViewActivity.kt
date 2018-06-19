package org.kinecosystem.kinit.view.earn

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.repository.QuestionnaireRepository
import org.kinecosystem.kinit.view.tutorial.BaseSingleFragmentActivity
import javax.inject.Inject


class TaskWebViewActivity : BaseSingleFragmentActivity() {

    companion object {
        fun getIntent(context: Context) = Intent(context, TaskWebViewActivity::class.java)
    }

    @Inject
    lateinit var questionnaireRepository: QuestionnaireRepository

    override fun inject() {
        KinitApplication.coreComponent.inject(this)
    }

    override fun getFragment(): Fragment {
        return when (questionnaireRepository.task!!.type) {
            "truex" -> TrueXWebFragment.getInstance()
            else -> TrueXWebFragment.getInstance()
        }
    }
}