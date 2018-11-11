package org.kinecosystem.kinit.view.earn

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.databinding.ObservableBoolean
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.MenuItem
import android.view.View
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.QuestionnaireFragmentLayoutBinding
import org.kinecosystem.kinit.model.earn.isQuiz
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.repository.CategoriesRepository
import org.kinecosystem.kinit.server.TaskService
import org.kinecosystem.kinit.util.GeneralUtils
import org.kinecosystem.kinit.view.BaseActivity
import org.kinecosystem.kinit.viewmodel.earn.QuestionnaireViewModel
import org.kinecosystem.kinit.viewmodel.earn.QuizViewModel
import javax.inject.Inject

class QuestionnaireActivity : BaseActivity(), QuestionnaireActions {

    @Inject
    lateinit var categoriesRepository: CategoriesRepository
    @Inject
    lateinit var taskService: TaskService

    private lateinit var model: QuestionnaireViewModel
    private lateinit var nextFragmentCallback: Observable.OnPropertyChangedCallback

    private val listener = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            if ((sender as ObservableBoolean).get()) {
                finish()
            }
        }
    }

    override fun next() {
        model.next()
    }

    override fun submissionAnimComplete() {
        model.submissionAnimComplete()
    }

    override fun transactionError() {
        model.transactionError()
    }

    override fun submissionError() {
        model.submissionError()
    }

    fun updateNoToolBar() {
        findViewById<View>(R.id.header_with_x_layout).visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        KinitApplication.coreComponent.inject(this)
        super.onCreate(savedInstanceState)
        val binding: QuestionnaireFragmentLayoutBinding = DataBindingUtil.setContentView(this,
                R.layout.questionnaire_fragment_layout)
        

        if (categoriesRepository.currentTaskInProgress == null)
            finish()

        categoriesRepository.currentTaskRepo?.task?.let {
            model = if (it.isQuiz()) {
                QuizViewModel(savedInstanceState != null, Navigator(this))
            } else {
                QuestionnaireViewModel(savedInstanceState != null, Navigator(this))
            }
            binding.model = model

            it.category_id?.let { it ->
                val category = categoriesRepository.getCategory(it)
                category?.uiData?.color?.let {
                    GeneralUtils.updateStatusBarColor(this, it)
                }
            }

            updateFragment(model.nextFragment.get())
            nextFragmentCallback = object : Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(p0: Observable?, p1: Int) {
                    updateFragment(model.nextFragment.get())
                }
            }
            model.nextFragment.addOnPropertyChangedCallback(nextFragmentCallback)
        }
    }

    override fun onResume() {
        super.onResume()
        model.shouldFinishActivity.addOnPropertyChangedCallback(listener)
    }

    override fun onPause() {
        super.onPause()
        model.shouldFinishActivity.removeOnPropertyChangedCallback(listener)
    }

    override fun onDestroy() {
        super.onDestroy()
        model.nextFragment.removeOnPropertyChangedCallback(nextFragmentCallback)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(menuItem)
    }

    private fun updateFragment(newFragment: Fragment) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
                    .replace(R.id.fragment_container, newFragment).commitNowAllowingStateLoss()
        } else {
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, newFragment)
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out).commitNowAllowingStateLoss()
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, QuestionnaireActivity::class.java)
        }
    }
}