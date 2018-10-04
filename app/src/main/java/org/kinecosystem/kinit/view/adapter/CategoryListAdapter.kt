package org.kinecosystem.kinit.view.adapter

import android.content.Context
import android.databinding.Observable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.model.earn.Category
import org.kinecosystem.kinit.model.spend.Offer
import org.kinecosystem.kinit.model.spend.isP2p
import org.kinecosystem.kinit.util.ImageUtils
import org.kinecosystem.kinit.viewmodel.earn.CategoryViewModel

class CategoryListAdapter(private val context: Context, private val model: CategoryViewModel)
    : RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {


    private var categories: List<Category> = model.getCategories()
    val propertyChangeCallBack = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            refresh()
        }
    }

    fun refresh() {
        categories = model.getCategories()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): CategoryListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.offer_card, parent, false)
        return ViewHolder(context, view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val offer: Offer = categories[position]
        holder.bind(offer)
        holder.view.setOnClickListener { model.onItemClicked(offer, position) }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        model.offersRepository.offers.removeOnPropertyChangedCallback(propertyChangeCallBack)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        model.offersRepository.offers.addOnPropertyChangedCallback(propertyChangeCallBack)
    }


    class ViewHolder(private val context: Context, val view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.offer_title)
        private val providerName: TextView = view.findViewById(R.id.provider_name)
        private val reward: TextView = view.findViewById(R.id.offer_reward)
        private val imageView: ImageView = view.findViewById(R.id.offer_image)
        private val logoView: ImageView = view.findViewById(R.id.offer_logo)

        fun bind(offer: Offer) {
            title.text = offer.title
            providerName.text = offer.provider!!.name
            if (offer.isP2p()) {
                reward.visibility = View.INVISIBLE
            } else {
                reward.text = offer.price!!.toString() + " KIN"
            }
            ImageUtils.loadImageIntoView(context, offer.imageUrl, imageView)
            ImageUtils.loadImageIntoView(context, offer.provider.imageUrl, logoView)
        }
    }
}