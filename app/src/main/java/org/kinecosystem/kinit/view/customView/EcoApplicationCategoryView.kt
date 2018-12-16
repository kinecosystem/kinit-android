package org.kinecosystem.kinit.view.customView

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.view.adapter.EcoAppCardListAdapter
import org.kinecosystem.kinit.viewmodel.spend.EcoAppsCategoryViewModel

class EcoApplicationCategoryView : ConstraintLayout {


    constructor(context: Context, categoryId: Int) : super(context) {
        init(context, categoryId)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, -1)
    }

    private fun init(context: Context, categoryId: Int) {
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.apps_category_layout, this, true)
        val model = EcoAppsCategoryViewModel(Navigator(context), categoryId)
        view.findViewById<TextView>(R.id.title).text = model.categoryTitle()
        val list = view.findViewById<RecyclerView>(R.id.list)
        list.isFocusable = false
        list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        list.adapter = EcoAppCardListAdapter(context, model)
    }


}
