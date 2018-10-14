package org.kinecosystem.kinit.view.earn

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.CategoryTaskLayoutBinding
import org.kinecosystem.kinit.repository.CategoriesRepository
import org.kinecosystem.kinit.repository.TasksRepository
import org.kinecosystem.kinit.view.BaseActivity
import org.kinecosystem.kinit.viewmodel.earn.CategoryTaskViewModel
import javax.inject.Inject

class CategoryTaskActivity : BaseActivity() {

    @Inject
    lateinit var categoriesRepository: CategoriesRepository
    @Inject
    lateinit var tasksRepository: TasksRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        KinitApplication.coreComponent.inject(this)
        super.onCreate(savedInstanceState)
        val binding: CategoryTaskLayoutBinding = DataBindingUtil.setContentView(this, R.layout.category_task_layout)
        val categoryId = intent.getStringExtra(CATEGORY_ID_PARAM)
        val category = categoriesRepository.getCategory(categoryId)
        category?.let {
            binding.model = CategoryTaskViewModel(it)
            updateStatusBar(it.uiData?.color)
        } ?: run {
            finish()
        }


    }

    private fun updateStatusBar(color: String?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor(color)
        }
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
