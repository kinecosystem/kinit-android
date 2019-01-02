package org.kinecosystem.tippic.view.earn

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.databinding.ObservableBoolean
import android.os.Bundle
import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.R
import org.kinecosystem.tippic.databinding.CategoryTaskLayoutBinding
import org.kinecosystem.tippic.navigation.Navigator
import org.kinecosystem.tippic.repository.CategoriesRepository
import org.kinecosystem.tippic.util.GeneralUtils
import org.kinecosystem.tippic.view.BaseActivity
import org.kinecosystem.tippic.viewmodel.earn.CategoryTaskViewModel
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
        TippicApplication.coreComponent.inject(this)
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
