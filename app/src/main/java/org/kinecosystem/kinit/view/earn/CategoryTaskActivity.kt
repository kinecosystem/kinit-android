package org.kinecosystem.kinit.view.earn

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.databinding.ObservableBoolean
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.CategoryTaskLayoutBinding
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.repository.CategoriesRepository
import org.kinecosystem.kinit.util.GeneralUtils
import org.kinecosystem.kinit.view.BaseActivity
import org.kinecosystem.kinit.viewmodel.earn.CategoryTaskViewModel
import javax.inject.Inject

class CategoryTaskActivity : BaseActivity() {

    @Inject
    lateinit var categoriesRepository: CategoriesRepository
    lateinit var model: CategoryTaskViewModel
    private val listener = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            if ((sender as ObservableBoolean).get()) {
                finish()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        KinitApplication.coreComponent.inject(this)
        super.onCreate(savedInstanceState)
        val binding: CategoryTaskLayoutBinding = DataBindingUtil.setContentView(this, R.layout.category_task_layout)
        val categoryId = intent.getStringExtra(CATEGORY_ID_PARAM)
        val category = categoriesRepository.getCategory(categoryId)
        val navigator = Navigator(this)
        category?.let {
            model = CategoryTaskViewModel(navigator, it)
            binding.model = model
            GeneralUtils.updateStatusBarColor(this, it.uiData?.color)
        } ?: run {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        model.onResume()
        model.shouldFinishActivity.addOnPropertyChangedCallback(listener)
    }

    override fun onPause() {
        super.onPause()
        model.shouldFinishActivity.removeOnPropertyChangedCallback(listener)
    }

    companion object {

        private const val CATEGORY_ID_PARAM = "CATEGORY_ID_PARAM"

        fun getIntent(context: Context, categoryId: String): Intent {
            val intent = Intent(context, CategoryTaskActivity::class.java)
            intent.putExtra(CATEGORY_ID_PARAM, categoryId)
            return intent
        }
    }


}
