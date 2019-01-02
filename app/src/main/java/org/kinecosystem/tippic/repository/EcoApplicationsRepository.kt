package org.kinecosystem.tippic.repository

import org.kinecosystem.tippic.model.spend.EcoApplication
import org.kinecosystem.tippic.server.api.EcoApplicationsApi

class EcoApplicationsRepository {
    var appCategories: List<EcoApplicationsApi.AppsCategory> = ArrayList()
        private set

    fun updateCategories(newAppCategories: List<EcoApplicationsApi.AppsCategory>?) {
        newAppCategories?.let {
            appCategories = it
        }
    }


    fun categoryTitle(categoryId: Int): String {
        val category = appCategories.singleOrNull { cat -> cat.id == categoryId }
        category?.let {
            return it.title
        }
        return ""
    }

    fun apps(categoryId: Int): List<EcoApplication> {
        val category = appCategories.singleOrNull { cat -> cat.id == categoryId }
        category?.let {
            return it.apps
        }
        return listOf()
    }

}