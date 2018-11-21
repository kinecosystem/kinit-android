package org.kinecosystem.kinit.view.phoneVerify;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.kinecosystem.kinit.KinitApplication;
import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.analytics.Analytics;
import org.kinecosystem.kinit.analytics.Events;
import org.kinecosystem.kinit.util.DeviceUtils;
import org.kinecosystem.kinit.view.BaseFragment;

import javax.inject.Inject;


public class PhoneSendFragment extends BaseFragment {

    public static final String TAG = PhoneSendFragment.class.getSimpleName();
    @Inject
    Analytics analytics;
    private static final int MIN_PHONE_LENGTH = 5;
    private static final String HAS_BACK = "HAS_BACK";
    private EditText prefix, phone;
    private TextView next, error;
    private View clear;
    private boolean phoneValid, prefixValid;
    private PhoneVerificationUIActions actions;

    public static PhoneSendFragment newInstance(boolean hasBack) {
        PhoneSendFragment fragment = new PhoneSendFragment();
        Bundle args = new Bundle();
        args.putBoolean(HAS_BACK, hasBack);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        KinitApplication.coreComponent.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.phone_send_fragment, container, false);
        if (getActivity() instanceof PhoneVerificationUIActions) {
            actions = (PhoneVerificationUIActions) getActivity();
        } else {
            Log.e(TAG, "activity must implements PhoneVerificationActions");
            getActivity().finish();
        }
        prefix = view.findViewById(R.id.prefix);
        phone = view.findViewById(R.id.phone_input);
        analytics.protectView(view);
        phone.requestFocus();
        next = view.findViewById(R.id.next);
        error = view.findViewById(R.id.error);
        clear = view.findViewById(R.id.clear);
        next.setEnabled(false);
        View back = view.findViewById(R.id.back);
        if (getArguments().getBoolean(HAS_BACK, false)) {
            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(view13 -> actions.onBackPressed(0));
        }
        clear.setOnClickListener(view12 -> phone.setText(""));
        String countryZipCode = DeviceUtils.Companion.getLocalDialPrefix(getActivity());
        if (!countryZipCode.isEmpty()) {
            prefix.setText(countryZipCode);
            prefix.setKeyListener(null);
            prefixValid = true;
        } else {
            prefix.setText("+");
            prefixValid = false;

        }
        next.setEnabled(true);
        next.setOnClickListener(view1 -> {
            onSendPhone();
            next.setEnabled(false);
        });
        prefix.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() >= 2) {
                    error.setVisibility(View.INVISIBLE);
                    prefixValid = true;
                } else {
                    error.setVisibility(View.VISIBLE);
                    prefixValid = false;
                }
                next.setEnabled(phoneValid && prefixValid);
            }
        });
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = editable.length();
                if (length >= 1) {
                    clear.setVisibility(View.VISIBLE);
                } else {
                    clear.setVisibility(View.INVISIBLE);
                }
                phoneValid = length > MIN_PHONE_LENGTH;
                next.setEnabled(phoneValid && prefixValid);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        analytics.logEvent(new Events.Analytics.ViewPhoneAuthPage());
    }

    public void onSendPhone() {
        String prefixStr = prefix.getText().toString();
        if (!prefixStr.startsWith("+")) {
            prefixStr = "+" + prefixStr;
        }
        String fullPhone = prefixStr + phone.getText();
        actions.onSendPhone(fullPhone);
        analytics.logEvent(new Events.Analytics.ClickNextButtonOnPhoneAuthPage());
    }
}
