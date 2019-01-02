package org.kinecosystem.tippic.view.adapter

import android.app.Activity
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.kinecosystem.tippic.R
import org.kinecosystem.tippic.model.spend.EcoApplication
import org.kinecosystem.tippic.model.spend.isKinTransferSupported
import org.kinecosystem.tippic.util.GeneralUtils
import org.kinecosystem.tippic.util.ImageUtils
import org.kinecosystem.tippic.viewmodel.spend.EcoAppsCategoryViewModel

class EcoAppCardListAdapter(private val context: Context, private val model: EcoAppsCategoryViewModel)
    : RecyclerView.Adapter<EcoAppCardListAdapter.ViewHolder>() {

    private var apps: List<EcoApplication> = model.apps()

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): EcoAppCardListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.app_card, parent, false)
        val maxWidth = GeneralUtils.getScreenWidth(context as Activity)
        view.layoutParams.width = (maxWidth * 0.9).toInt()
        return ViewHolder(context, view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val app: EcoApplication = apps[position]
        holder.bind(app)
        holder.view.setOnClickListener { model.onItemClicked(app) }
        holder.actionBtn.setOnClickListener { model.onActionBtnClicked(app) }
    }

    override fun getItemCount(): Int {
        return apps.size
    }

    class ViewHolder(private val context: Context, val view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.app_name)
        private val info: TextView = view.findViewById(R.id.appInfo)
        val actionBtn: TextView = view.findViewById(R.id.btn)
        private val imageView: ImageView = view.findViewById(R.id.app_image)
        private val iconView: ImageView = view.findViewById(R.id.app_icon)

        fun bind(app: EcoApplication) {
            name.text = app.name
            info.text = app.data.descriptionShort
            if (app.isKinTransferSupported()) {
                actionBtn.text = context.resources.getString(R.string.send_kin)
                actionBtn.setTextColor(ContextCompat.getColor(context, R.color.white))
                actionBtn.setBackgroundResource(R.drawable.full_rounded_blue)
            } else {
                actionBtn.text = context.resources.getString(R.string.get_app)
                actionBtn.setTextColor(ContextCompat.getColor(context, R.color.blue))
                actionBtn.setBackgroundResource(R.drawable.empty_rounded_blue)
            }

            ImageUtils.loadImageIntoView(context, app.data.cardImageUrl, imageView)
            ImageUtils.loadImageIntoView(context, app.data.iconUrl, iconView)
        }
    }
}