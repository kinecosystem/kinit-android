package org.kinecosystem.kinit.view.earn

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.MenuItem
import android.view.View
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.QuestionnaireFragmentLayoutBinding
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.navigation.Navigator.Destination
import org.kinecosystem.kinit.repository.QuestionnaireRepository
import org.kinecosystem.kinit.view.BaseActivity
import org.kinecosystem.kinit.viewmodel.earn.QuestionnaireViewModel
import javax.inject.Inject

class QuestionnaireActivity : BaseActivity(), QuestionnaireActions {

    @Inject
    lateinit var questionnaireRepository: QuestionnaireRepository
    private lateinit var questionnaireModel: QuestionnaireViewModel
    private lateinit var nextFragmentCallback: Observable.OnPropertyChangedCallback

    override fun nextQuestion() {
        questionnaireModel.nextQuestion()
    }

    override fun submissionComplete() {
        questionnaireModel.submissionComplete()
    }

    override fun transactionError() {
        questionnaireModel.transactionError()
    }

    override fun submissionError() {
        questionnaireModel.submissionError()
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, QuestionnaireActivity::class.java)
        }
    }

    fun updateNoToolBar() {
        findViewById<View>(R.id.header_with_x_layout).visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        KinitApplication.coreComponent.inject(this)
        super.onCreate(savedInstanceState)
        val binding: QuestionnaireFragmentLayoutBinding = DataBindingUtil.setContentView(this,
            R.layout.questionnaire_fragment_layout)
        questionnaireModel = QuestionnaireViewModel(savedInstanceState != null)
        binding.model = questionnaireModel

        findViewById<View>(R.id.header_x_btn).setOnClickListener { view ->
            val navigator = Navigator(this@QuestionnaireActivity)
            navigator.navigateTo(Destination.MAIN_SCREEN)
            questionnaireRepository.resetTaskState()
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out)
            finish()
        }

        updateFragment(questionnaireModel.nextFragment.get())
        nextFragmentCallback = object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(p0: Observable?, p1: Int) {
                updateFragment(questionnaireModel.nextFragment.get())
            }
        }
        questionnaireModel.nextFragment.addOnPropertyChangedCallback(nextFragmentCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        questionnaireModel.nextFragment.removeOnPropertyChangedCallback(nextFragmentCallback)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(menuItem)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out)
        questionnaireModel.onBackPressed()
    }

    private fun updateFragment(newFragment: Fragment) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
                .replace(R.id.fragment_container, newFragment).commitAllowingStateLoss()
        } else {
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, newFragment)
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out).commitAllowingStateLoss()
        }
    }
}
