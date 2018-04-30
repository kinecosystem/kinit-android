package org.kinecosystem.kinit.view.spend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.view.BaseActivity;
import org.kinecosystem.kinit.viewmodel.spend.PurchaseOfferViewModel;

public class PurchaseOfferActivity extends BaseActivity {

    private static final String INDEX_PARAM = "SpendActivity_index_param";

    public static Intent getIntent(Context context, int index) {
        Intent intent = new Intent(context, PurchaseOfferActivity.class);
        intent.putExtra(INDEX_PARAM, index);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fragment_layout);
        int index = getIntent().getIntExtra(INDEX_PARAM, 0);
        PurchaseOfferFragment fragment = PurchaseOfferFragment.newInstance(index);
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
