package org.kinecosystem.kinit.repository

import android.databinding.ObservableBoolean
import org.kinecosystem.kinit.model.spend.EcosystemApp
import org.kinecosystem.kinit.model.spend.isKinTransferSupported
import org.kinecosystem.kinit.server.api.EcoApplicationsApi

class EcoApplicationsRepository {
    var appCategories: List<EcoApplicationsApi.AppsCategory> = ArrayList()
        private set
    var hasAppToSendObservable: ObservableBoolean = ObservableBoolean(false)
        private set

    fun updateCategories(newAppCategories: List<EcoApplicationsApi.AppsCategory>?) {
        newAppCategories?.let {
            appCategories = it
            hasAppToSendObservable.set(hasAppToSend())
        }
    }


    fun categoryTitle(categoryId: Int): String {
        val category = appCategories.singleOrNull { cat -> cat.id == categoryId }
        category?.let {
            return it.title
        }
        return ""
    }

    fun apps(categoryId: Int): List<EcosystemApp> {
        val category = appCategories.singleOrNull { cat -> cat.id == categoryId }
        category?.let {
            return it.apps
        }
        return listOf()
    }

    private fun hasAppToSend(): Boolean {
        for (category in appCategories) {
            for (app in category.apps) {
                if (app.isKinTransferSupported()) {
                    return true
                }
            }
        }
        return false
    }

}