package org.kinecosystem.tippic.view.spend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import org.kinecosystem.tippic.R;
import org.kinecosystem.tippic.model.spend.Offer;
import org.kinecosystem.tippic.view.BaseActivity;
import org.kinecosystem.tippic.viewmodel.spend.PurchaseOfferViewModel;

public class PurchaseOfferActivity extends BaseActivity {

    private static final String OFFER_PARAM = "SpendActivity_offer_param";

    public static Intent getIntent(Context context, Offer offer) {
        Intent intent = new Intent(context, PurchaseOfferActivity.class);
        intent.putExtra(OFFER_PARAM, offer);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fragment_layout);
        Offer offer = getIntent().getParcelableExtra(OFFER_PARAM);
        PurchaseOfferFragment fragment = PurchaseOfferFragment.newInstance(offer);
        getSupportFragmentManager().beginTransaction()
            .add(R.id.fragment_container, fragment).commit();
    }


    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment != null && fragment instanceof PurchaseOfferFragment) {
            PurchaseOfferViewModel model = ((PurchaseOfferFragment) fragment).getModel();
            if (model != null) {
                model.onCloseButtonClicked(fragment.getView());
                return;
            }
        }

        // fragment or model is null
        super.onBackPressed();
    }

}
