package org.kinecosystem.kinit.viewmodel.earn

import android.databinding.ObservableField
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.blockchain.Wallet
import org.kinecosystem.kinit.model.earn.Category
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.repository.CategoriesRepository
import org.kinecosystem.kinit.server.CategoriesService
import org.kinecosystem.kinit.server.TaskService
import org.kinecosystem.kinit.util.Scheduler
import org.kinecosystem.kinit.view.TabViewModel
import javax.inject.Inject

class CategoriesViewModel(private val navigator: Navigator) : TabViewModel {

    @Inject
    lateinit var categoriesRepository: CategoriesRepository
    @Inject
    lateinit var walletService: Wallet
    @Inject
    lateinit var categoriesService: CategoriesService
    @Inject
    lateinit var taskService: TaskService
    @Inject
    lateinit var scheduler: Scheduler
    @Inject
    lateinit var analytics: Analytics

    var title: ObservableField<String>
    var subtitle: ObservableField<String>
    var balance: ObservableField<String>

    init {
        KinitApplication.coreComponent.inject(this)
        balance = walletService.balance
        categoriesService.retrieveCategories()
        //reset data
        categoriesRepository.onCategorySelected()
        title = categoriesRepository.headerTitle
        subtitle = categoriesRepository.headerSubtitle
    }

    override fun onScreenVisibleToUser() {
        loadData()
    }

    fun onItemClicked(category: Category, position: Int) {
        categoriesRepository.onCategorySelected(category)
        navigator.navigateToCategory(category.id)
    }

    private fun loadData() {
        categoriesService.retrieveCategories()
    }

}

