package org.kinecosystem.kinit.view.adapter

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.model.earn.Category
import org.kinecosystem.kinit.model.earn.Task
import org.kinecosystem.kinit.model.earn.hasTask
import org.kinecosystem.kinit.model.earn.setTask
import org.kinecosystem.kinit.viewmodel.earn.EarnCategoriesViewModel


class CategoryListAdapter(private val context: Context, private val model: EarnCategoriesViewModel)
    : RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {


    private var categories: List<Category> = model.categories()
//    val propertyChangeCallBack = object : Observable.OnPropertyChangedCallback() {
//        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
//            refresh()
//        }
//    }

    fun refresh() {
        categories = model.categories()
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
            if (!category.hasTask()) {
                category.setTask(Task("44"))
            }else{
                categories[0].setTask(null)
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        // model.offersRepository.offers.removeOnPropertyChangedCallback(propertyChangeCallBack)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        //model.offersRepository.offers.addOnPropertyChangedCallback(propertyChangeCallBack)
    }


    class ViewHolder(private val context: Context, val view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.title)
        private val imageView: ImageView = view.findViewById(R.id.image)

        fun bind(category: Category) {
            title.text = category.title
            if (!category.hasTask()) {
                title.text = "BW"
                val matrix = ColorMatrix()
                matrix.setSaturation(0f)
                imageView.colorFilter = ColorMatrixColorFilter(matrix)
            } else {
                imageView.colorFilter = null

            }
            // ImageUtils.loadImageIntoView(context, category.imageUrl, imageView)
        }
    }
}