package org.kinecosystem.kinit.viewmodel.earn

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.util.Log
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
import org.kinecosystem.kinit.viewmodel.backup.BackupAlertManager
import javax.inject.Inject

class CategoriesViewModel(private val backupAlertManager: BackupAlertManager?) : TabViewModel {

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
    @Inject
    lateinit var navigator: Navigator

    var shouldShowNoTask = ObservableBoolean(false)

    var title: ObservableField<String>
    var subtitle: ObservableField<String>
    var balance: ObservableField<String>

    private var scheduledRunnable: Runnable? = null

    init {
        KinitApplication.coreComponent.inject(this)
        balance = walletService.balance
        categoriesService.retrieveCategories()
        title = categoriesRepository.headerTitle
        subtitle = categoriesRepository.headerSubtitle
        refresh()
    }

    private fun refresh() {
        loadData()
        handleEmptyStates()
    }

    private fun loadData() {
        categoriesService.retrieveCategories()
        taskService.retrieveNextTask()
    }

    private fun handleEmptyStates() {
    }


    override fun onScreenVisibleToUser() {
        refresh()
    }

    fun onItemClicked(category: Category, position: Int) {
        val task = categoriesRepository.getTask(category)
        if (task!= null) {
            Log.d("####", "#### task for cat ${category.id} $task")

        }else{
            Log.d("####", "#### task for cat ${category.id} NULL")

        }
        navigator.navigateTo(category)
    }
}

