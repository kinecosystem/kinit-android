package org.kinecosystem.kinit.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.model.spend.Offer;
import org.kinecosystem.kinit.model.spend.OfferKt;
import org.kinecosystem.kinit.util.ImageUtils;
import org.kinecosystem.kinit.viewmodel.spend.SpendViewModel;

public class OfferListAdapter extends RecyclerView.Adapter<OfferListAdapter.ViewHolder> {


    private List<Offer> offerList;
    private Context context;
    private SpendViewModel model;

    public OfferListAdapter(Context context, SpendViewModel model) {
        this.context = context;
        this.model = model;
        offerList = model.getOffers();
    }

    @Override
    public OfferListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
        int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.offer_card, parent, false);
        ViewHolder vh = new ViewHolder(context, view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(offerList.get(position));
        holder.view.setOnClickListener(view -> model.onItemClicked(position));
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title, providerName, reward;
        private ImageView imageView, logoView;
        private Context context;
        public View view;

        public ViewHolder(Context context, View view) {
            super(view);
            this.view = view;
            this.context = context;
            title = view.findViewById(R.id.offer_title);
            providerName = view.findViewById(R.id.provider_name);
            reward = view.findViewById(R.id.offer_reward);
            imageView = view.findViewById(R.id.offer_image);
            logoView = view.findViewById(R.id.offer_logo);
        }

        public void bind(Offer offer) {
            title.setText(offer.getTitle());
            providerName.setText(offer.getProvider().getName());
            if (OfferKt.isP2p(offer)) {
                reward.setVisibility(View.INVISIBLE);
            } else {
                reward.setText(offer.getPrice() + " KIN");
            }
            ImageUtils.loadImageIntoView(context, offer.getImageUrl(), imageView);
            ImageUtils.loadImageIntoView(context, offer.getProvider().getImageUrl(), logoView);
        }
    }
}