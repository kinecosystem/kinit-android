package org.kinecosystem.tippic.viewmodel.earn

import android.databinding.Observable
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.analytics.Analytics
import org.kinecosystem.tippic.analytics.Events
import org.kinecosystem.tippic.blockchain.Wallet
import org.kinecosystem.tippic.model.earn.Category
import org.kinecosystem.tippic.navigation.Navigator
import org.kinecosystem.tippic.repository.CategoriesRepository
import org.kinecosystem.tippic.server.CategoriesService
import org.kinecosystem.tippic.server.NetworkServices
import org.kinecosystem.tippic.server.OperationCompletionCallback
import org.kinecosystem.tippic.server.TaskService
import org.kinecosystem.tippic.view.TabViewModel
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
    val isLoading = ObservableBoolean(false)


    init {
        TippicApplication.coreComponent.inject(this)
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
        if (this.categoriesRepository.categories.get().isEmpty()) {
            isLoading.set(true)
            showData.set(false)
        }
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

    fun onDataLoaded() {
        isLoading.set(false)
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
                    Events.Analytics.ViewErrorPage(Analytics.VIEW_ERROR_TYPE_GENERIC, Analytics.SERVER_ERROR_RESPONSE)
                }

            })
        }
    }


}

