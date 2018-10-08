package org.kinecosystem.kinit.view.earn

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.MenuItem
import android.view.View
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.QuestionnaireFragmentLayoutBinding
import org.kinecosystem.kinit.databinding.TaskLayoutBinding
import org.kinecosystem.kinit.model.earn.isQuiz
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.repository.TasksRepository
import org.kinecosystem.kinit.server.TaskService
import org.kinecosystem.kinit.view.BaseActivity
import org.kinecosystem.kinit.viewmodel.earn.QuestionnaireViewModel
import org.kinecosystem.kinit.viewmodel.earn.QuizViewModel
import javax.inject.Inject

class TaskActivity : BaseActivity() {

    @Inject
    lateinit var taskRepo: TasksRepository
    @Inject
    lateinit var taskService: TaskService

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, TaskActivity::class.java)
        }
    }

//    fun updateNoToolBar() {
//        findViewById<View>(R.id.header_with_x_layout).visibility = View.GONE
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        KinitApplication.coreComponent.inject(this)
        super.onCreate(savedInstanceState)
        val binding: TaskLayoutBinding = DataBindingUtil.setContentView(this,
                R.layout.task_layout)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor("#8f65ef")
        }




//        if (taskRepo.task == null)
//            finish()
//
//        taskRepo.task?.let {
//            questionnaireModel = if (it.isQuiz()) {
//                QuizViewModel(savedInstanceState != null)
//            } else {
//                QuestionnaireViewModel(savedInstanceState != null)
//            }
//            binding.model = questionnaireModel
//
//            findViewById<View>(R.id.header_x_btn).setOnClickListener { view ->
//                val navigator = Navigator(this@QuestionnaireActivity)
//                navigator.navigateTo(Navigator.Destination.MAIN_SCREEN)
//                onBackPressed()
//                finish()
//            }
//
//            updateFragment(questionnaireModel.nextFragment.get())
//            nextFragmentCallback = object : Observable.OnPropertyChangedCallback() {
//                override fun onPropertyChanged(p0: Observable?, p1: Int) {
//                    updateFragment(questionnaireModel.nextFragment.get())
//                }
//            }
//            questionnaireModel.nextFragment.addOnPropertyChangedCallback(nextFragmentCallback)
//        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//       // questionnaireModel.nextFragment.removeOnPropertyChangedCallback(nextFragmentCallback)
//    }

//    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
//        if (menuItem.itemId == android.R.id.home) {
//            onBackPressed()
//        }
//        return super.onOptionsItemSelected(menuItem)
//    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        //taskRepo.resetTaskState()
//        //overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out)
//       // questionnaireModel.onBackPressed()
//    }

//    private fun updateFragment(newFragment: Fragment) {
//        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
//        if (fragment != null) {
//            supportFragmentManager.beginTransaction()
//                    .setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
//                    .replace(R.id.fragment_container, newFragment).commitNowAllowingStateLoss()
//        } else {
//            supportFragmentManager.beginTransaction().add(R.id.fragment_container, newFragment)
//                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out).commitNowAllowingStateLoss()
//        }
//    }
}