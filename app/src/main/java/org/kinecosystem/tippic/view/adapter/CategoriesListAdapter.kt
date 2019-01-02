package org.kinecosystem.tippic.view.adapter

import android.content.Context
import android.databinding.Observable
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.R
import org.kinecosystem.tippic.model.earn.Category
import org.kinecosystem.tippic.model.earn.isEnabled
import org.kinecosystem.tippic.repository.CategoriesRepository
import org.kinecosystem.tippic.util.ImageUtils
import org.kinecosystem.tippic.viewmodel.earn.CategoriesViewModel
import javax.inject.Inject


class CategoryListAdapter(private val context: Context, private val model: CategoriesViewModel)
    : RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {

    @Inject
    lateinit var categoriesRepository: CategoriesRepository

    private lateinit var categories: List<Category>

    private val onDataChanged = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            refresh()
        }

    }

    init {
        TippicApplication.coreComponent.inject(this)
        refresh()
    }

    fun refresh() {
        categories = if (categoriesRepository.categories.get() != null) {
            categoriesRepository.categories.get()
        } else {
            listOf()
        }
        model.onDataLoaded()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): CategoryListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.category_item, parent, false)
        return ViewHolder(context, view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category: Category = categories[position]
        holder.bind(category)
        holder.view.setOnClickListener {
            model.onItemClicked(category, position)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        categoriesRepository.categories.removeOnPropertyChangedCallback(onDataChanged)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        categoriesRepository.categories.addOnPropertyChangedCallback(onDataChanged)
    }


    class ViewHolder(private val context: Context, val view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.image)

        fun bind(category: Category) {
            category.uiData?.let {
                    ImageUtils.loadImageIntoView(context, it.imageUrl, imageView)
            }
            if (!category.isEnabled()) {
                val matrix = ColorMatrix()
                matrix.setSaturation(0f)
                imageView.colorFilter = ColorMatrixColorFilter(matrix)
            } else {
                imageView.colorFilter = null

            }
        }
    }
}