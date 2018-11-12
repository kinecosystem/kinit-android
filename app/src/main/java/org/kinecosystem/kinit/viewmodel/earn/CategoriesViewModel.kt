package org.kinecosystem.kinit.viewmodel.earn

import android.databinding.Observable
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.blockchain.Wallet
import org.kinecosystem.kinit.model.earn.Category
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.repository.CategoriesRepository
import org.kinecosystem.kinit.server.CategoriesService
import org.kinecosystem.kinit.server.NetworkServices
import org.kinecosystem.kinit.server.OperationCompletionCallback
import org.kinecosystem.kinit.server.TaskService
import org.kinecosystem.kinit.view.TabViewModel
import javax.inject.Inject

open class CategoriesViewModel(private val navigator: Navigator) : TabViewModel {

    @Inject
    lateinit var categoriesRepository: CategoriesRepository
    @Inject
    lateinit var walletService: Wallet
    @Inject
    lateinit var categoriesService: CategoriesService
    @Inject
    lateinit var taskService: TaskService
    @Inject
    lateinit var networkServices: NetworkServices
    @Inject
    lateinit var analytics: Analytics

    var title: ObservableField<String>
    var subtitle: ObservableField<String>
    var balance: ObservableField<String>
    val hasErrors = ObservableBoolean(false)
    val hasNetwork = ObservableBoolean(true)
    val showData = ObservableBoolean(true)


    init {
        KinitApplication.coreComponent.inject(this)
        balance = walletService.balance
        categoriesService.retrieveCategories()
        //reset data
        categoriesRepository.onCategorySelected()
        title = categoriesRepository.headerTitle
        subtitle = categoriesRepository.headerSubtitle
        hasNetwork.set(networkServices.isNetworkConnected())
        showData.set(!hasErrors.get() && hasNetwork.get())
        hasErrors.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (networkServices.isNetworkConnected() && sender is ObservableBoolean) {
                    showData.set(!sender.get())
                }
            }
        })
    }

    override fun onScreenVisibleToUser() {
        if (networkServices.isNetworkConnected()) {
            hasNetwork.set(true)
            loadData()
        } else {
            hasNetwork.set(false)
            showData.set(false)
        }
        analytics.logEvent(Events.Analytics.ViewTaskCategoriesPage())
    }

    fun onItemClicked(category: Category, position: Int) {
        categoriesRepository.onCategorySelected(category)
        navigator.navigateToCategory(category.id)
        analytics.logEvent(Events.Analytics.ClickCategoryButtonOnTaskCategoriesPage(category.title))
    }

    private fun loadData() {
        if (networkServices.isNetworkConnected()) {
            categoriesService.retrieveCategories(object : OperationCompletionCallback {
                override fun onSuccess() {
                    if (!categoriesRepository.hasAnyTask()) {
                        taskService.retrieveAllTasks()
                    }
                    hasErrors.set(false)
                    showData.set(true)
                }

                override fun onError(errorCode: Int) {
                    showData.set(false)
                    hasErrors.set(true)
                }

            })
        }
    }

}

