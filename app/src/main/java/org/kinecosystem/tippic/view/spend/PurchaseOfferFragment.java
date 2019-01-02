package org.kinecosystem.tippic.view.spend;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.Observable.OnPropertyChangedCallback;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import org.kinecosystem.tippic.R;
import org.kinecosystem.tippic.TippicApplication;
import org.kinecosystem.tippic.databinding.SpendDetailLayoutBinding;
import org.kinecosystem.tippic.model.spend.Offer;
import org.kinecosystem.tippic.navigation.Navigator;
import org.kinecosystem.tippic.repository.OffersRepository;
import org.kinecosystem.tippic.view.BaseFragment;
import org.kinecosystem.tippic.viewmodel.spend.PurchaseOfferViewModel;

import javax.inject.Inject;

public class PurchaseOfferFragment extends BaseFragment implements PurchaseOfferActions {

    public static final String ARG_OFFER = "PurchaseOfferFragment_ARG_OFFER_INDEX";
    public static final String TAG = PurchaseOfferFragment.class.getSimpleName();
    @Inject
    OffersRepository offersRepository;
    private SpendDetailLayoutBinding binding;
    private PurchaseOfferViewModel model;
    private AlertDialog alertDialog;

    public static PurchaseOfferFragment newInstance(Offer offer) {
        PurchaseOfferFragment fragment = new PurchaseOfferFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_OFFER, offer);
        fragment.setArguments(args);
        return fragment;
    }

    public PurchaseOfferViewModel getModel() {
        return model;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.spend_detail_layout, container, false);
        TippicApplication.coreComponent.inject(this);
        Offer offer = getArguments().getParcelable(ARG_OFFER);
        if (offer == null) {
            getActivity().finish();
            Log.e(TAG, "Offer could not be found");
        }
        Navigator navigator = new Navigator(getActivity());
        model = new PurchaseOfferViewModel(navigator, offer);
        model.setPurchaseOfferActions(this);
        binding.info.setMovementMethod(new ScrollingMovementMethod());
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

    @Override
    public void updateBuyButtonWidth() {
        binding.codeButton.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (binding.codeButton.getWidth() > 0) {
                    binding.codeButton.getViewTreeObserver().removeOnGlobalLayoutListener(this::onGlobalLayout);
                    binding.buyBtn.expand(binding.codeButton.getMeasuredWidth());
                }
            }
        });
    }
}
