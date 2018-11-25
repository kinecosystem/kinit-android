package org.kinecosystem.kinit.repository

import android.util.Log
import org.kinecosystem.kinit.model.spend.EcoApplication
import org.kinecosystem.kinit.server.api.EcoApplicationsApi

class EcoApplicationsRepository {
    var appCategories: List<EcoApplicationsApi.AppsCategory> = ArrayList()
        private set

    fun updateCategories(newAppCategories: List<EcoApplicationsApi.AppsCategory>?) {
        newAppCategories?.let {
            appCategories = it
        }
        Log.d("###", "### got updateCategories apps $newAppCategories")
    }


    fun categoryTitle(categoryId:Int) : String {
        val category = appCategories.singleOrNull { cat -> cat.id == categoryId }
        category?.let {
            if(it.title.isNullOrEmpty()){
                return "title server null"
            }
            return it.title
        }
        return ""
    }

    fun apps(categoryId:Int) : List<EcoApplication>{
        val category = appCategories.singleOrNull { cat -> cat.id == categoryId }
        category?.let {
            return it.apps
        }
        return  listOf()
    }

}