package org.kinecosystem.kinit.view.spend;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.Observable.OnPropertyChangedCallback;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.databinding.SpendDetailLayoutBinding;
import org.kinecosystem.kinit.navigation.Navigator;
import org.kinecosystem.kinit.view.BaseFragment;
import org.kinecosystem.kinit.viewmodel.spend.PurchaseOfferViewModel;

public class PurchaseOfferFragment extends BaseFragment implements PurchaseOfferActions {

    public static final String ARG_OFFER_INDEX = "SpendOfferFragment_ARG_QUESTION_INDEX";
    public static final int INVALID_OFFER_INDEX = -1;
    public static final String TAG = PurchaseOfferFragment.class.getSimpleName();
    private SpendDetailLayoutBinding binding;
    private PurchaseOfferViewModel model;
    private AlertDialog alertDialog;

    public PurchaseOfferViewModel getModel() {
        return model;
    }

    public static PurchaseOfferFragment newInstance(int offerIndex) {
        PurchaseOfferFragment fragment = new PurchaseOfferFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_OFFER_INDEX, offerIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.spend_detail_layout, container, false);
        int index = getArguments().getInt(ARG_OFFER_INDEX, INVALID_OFFER_INDEX);
        if (index == INVALID_OFFER_INDEX) {
            getActivity().finish();
            Log.e(TAG, "Wrong offer index");
        }
        Navigator navigator = new Navigator(getActivity());
        model = new PurchaseOfferViewModel(getCoreComponents(), navigator, index);
        model.setPurchaseOfferActions(this);
        binding.setModel(model);
        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        model.onResume();
    }

    @Override
    public void animateBuy() {
        model.getCouponPurchaseCompleted().addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                binding.progress.setVisibility(View.GONE);
            }
        });
        binding.progress.postDelayed(() -> {
            binding.progress.setAlpha(0);
            binding.progress.setVisibility(View.VISIBLE);
            binding.progress.animate().alpha(1).setDuration(750);
        }, 450);
        binding.buyBtn.onBuy(binding.codeButton.getMeasuredWidth());
    }

    @Override
    public void closeScreen() {
        getActivity().finish();
    }

    @Override
    public void shareCode(String code) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, code);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share_code_to)));
    }

    @Override
    public void showDialog(int titleStringRes, int messageStringRes, int actionStringRes, boolean finish,
        String errorType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        if (errorType != null) {
            model.logViewErrorPopup(errorType);
        }
        builder.setTitle(titleStringRes).setMessage(messageStringRes).setPositiveButton(actionStringRes,
            (dialogInterface, i) -> {
                if (errorType != null) {
                    model.logCloseErrorPopupClicked(errorType);
                }
                dialogInterface.dismiss();
                if (finish) {
                    getActivity().finish();
                }
            });
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (model != null) {
            model.setPurchaseOfferActions(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (model != null) {
            model.setPurchaseOfferActions(null);
        }
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }
}
