package org.kinecosystem.kinit.view.phoneVerify;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.kinecosystem.kinit.KinitApplication;
import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.analytics.Analytics;
import org.kinecosystem.kinit.analytics.Events;
import org.kinecosystem.kinit.navigation.Navigator;
import org.kinecosystem.kinit.repository.DataStore;
import org.kinecosystem.kinit.repository.DataStoreProvider;
import org.kinecosystem.kinit.repository.UserRepository;
import org.kinecosystem.kinit.util.GeneralUtils;
import org.kinecosystem.kinit.util.SupportUtil;
import org.kinecosystem.kinit.view.BaseFragment;
import org.kinecosystem.kinit.view.support.SupportActivity;
import org.kinecosystem.kinit.viewmodel.SupportViewModel;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import static org.kinecosystem.kinit.view.phoneVerify.PhoneVerifyActivity.PHONE_AUTH_DATA_STORE;

public class CodeVerificationFragment extends BaseFragment {

    public static final String TAG = CodeVerificationFragment.class.getSimpleName();
    private static final String KEY_RESEND_CODE = "KEY_RESEND_CODE";
    private static final String PHONE_NUMBER = "PHONE_NUMBER";
    private static final String SHOW_CONTACT_SUPPORT = "SHOW_CONTACT_SUPPORT";
    private static final long COUNT_DOWN = 16 * DateUtils.SECOND_IN_MILLIS;
    private static final long CODE_LENGTH = 6;
    private static final long VIBRATE_DURATION = 500;
    private static final int MAX_ERROR_COUNT = 3;
    @Inject
    Analytics analytics;
    @Inject
    DataStoreProvider dataStoreProvider;
    @Inject
    UserRepository userRepository;

    private TextView[] inputs = new TextView[6];
    private View[] lines = new View[6];
    private TextView code, next, subtitle, counter, resend;
    private ProgressBar progressBar;
    private PhoneVerificationUIActions actions;
    private View hitArea;
    private int codeErrorCount = 0;
    private CountDownTimer timer = new CountDownTimer(COUNT_DOWN, DateUtils.SECOND_IN_MILLIS) {
        @Override
        public void onTick(long l) {
            String counterText = getResources().getString(R.string.code_sms_counter_subtitle, l / 1000);
            counter.setText(counterText);
        }

        @Override
        public void onFinish() {
            counter.setVisibility(View.GONE);
            resend.setVisibility(View.VISIBLE);
        }
    };


    public static CodeVerificationFragment newInstance(String phone, boolean showContactSupport) {
        CodeVerificationFragment fragment = new CodeVerificationFragment();
        Bundle args = new Bundle();
        args.putString(PHONE_NUMBER, phone);
        args.putBoolean(SHOW_CONTACT_SUPPORT, showContactSupport);
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
        View view = inflater.inflate(R.layout.phone_code_verify_fragment, container, false);
        if (getActivity() instanceof PhoneVerificationUIActions) {
            actions = (PhoneVerificationUIActions) getActivity();
        } else {
            Log.e(TAG, "activity must implements PhoneVerificationActions");
            getActivity().finish();
        }
        inputs[0] = view.findViewById(R.id.input0);
        inputs[1] = view.findViewById(R.id.input1);
        inputs[2] = view.findViewById(R.id.input2);
        inputs[3] = view.findViewById(R.id.input3);
        inputs[4] = view.findViewById(R.id.input4);
        inputs[5] = view.findViewById(R.id.input5);

        lines[0] = view.findViewById(R.id.line0);
        lines[1] = view.findViewById(R.id.line1);
        lines[2] = view.findViewById(R.id.line2);
        lines[3] = view.findViewById(R.id.line3);
        lines[4] = view.findViewById(R.id.line4);
        lines[5] = view.findViewById(R.id.line5);

        hitArea = view.findViewById(R.id.hitArea);
        hitArea.setOnClickListener(viewHit -> showKeyboardFocusCodeInput(viewHit));
        for (int i = 0; i < inputs.length; i++) {
            inputs[i].setText("");
        }
        code = view.findViewById(R.id.code_input);
        code.setFocusable(true);
        code.requestFocus();
        code.setY(0);
        code.setX(50000);
        counter = view.findViewById(R.id.counter);
        String counterText = getResources().getString(R.string.code_sms_counter_subtitle, 15);
        counter.setText(counterText);
        final String phoneNumber = getArguments().getString(PHONE_NUMBER);
        final boolean shouldShowContactSupport = getArguments().getBoolean(SHOW_CONTACT_SUPPORT, false);
        subtitle = view.findViewById(R.id.subtitle);
        String subtitleStr = getResources().getString(R.string.verification_code_subtitle, phoneNumber);
        subtitle.setText(subtitleStr);
        analytics.protectView(subtitle);
        resend = view.findViewById(R.id.resend);
        if (shouldShowContactSupport) {
            showContactSupport();
        } else {
            resend.setOnClickListener(resend -> {
                sendEvent();
                actions.onBackPressed(1);
            });
        }
        resend.setVisibility(View.GONE);
        next = view.findViewById(R.id.next);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        next.setClickable(true);
        next.setOnClickListener(view1 -> onSendCode());
        view.findViewById(R.id.back).setOnClickListener(view13 -> actions.onBackPressed(1));
        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int c = 0;
                for (; c < charSequence.length(); c++) {
                    inputs[c].setText("" + charSequence.charAt(c));
                }
                for (; c < inputs.length; c++) {
                    inputs[c].setText("");
                }
                next.setEnabled(charSequence.length() >= CODE_LENGTH);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        timer.start();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        analytics.logEvent(new Events.Analytics.ViewVerificationPage());
    }

    private void showKeyboardFocusCodeInput(final View trigger) {
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (trigger != null) {
                trigger.clearFocus();
            }
            if(code != null) {
                code.requestFocus();
                GeneralUtils.openKeyboard(getActivity(), code);
            }
        }, 50);
    }

    private void sendEvent() {
        DataStore dataStore = dataStoreProvider.dataStore(PHONE_AUTH_DATA_STORE);
        int resendCodeCount = dataStore.getInt(KEY_RESEND_CODE, 0);
        resendCodeCount++;
        analytics.logEvent(new Events.Analytics.ClickNewCodeLinkOnVerificationPage(resendCodeCount));
        dataStore.putInt(KEY_RESEND_CODE, resendCodeCount);
    }

    public void onSendCode() {
        next.setEnabled(false);
        actions.onSendCode(code.getText().toString());
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        timer.cancel();
        timer = null;
    }

    public void onError() {
        progressBar.setVisibility(View.GONE);
        vibrate();
        animateWiggle();
        code.setText("");
        next.setEnabled(false);
        codeErrorCount++;
        if (codeErrorCount >= MAX_ERROR_COUNT) {
            showContactSupport();
        }
        analytics.logEvent(new Events.Analytics.ViewErrorMessageOnVerificationPage());

    }

    private void showContactSupport() {
        resend.setText(getResources().getString(R.string.contact_support) + " >");
        resend.setOnClickListener(resend -> {
            HashMap<String, String> urlParams = new HashMap<>();
            urlParams.put("category", "Other");
            urlParams.put("subCategory", "On-boarding error");
            Intent intent = SupportActivity.Companion.getIntent(getContext(), SupportViewModel.Destination.CONTACT_US, urlParams);
            (new Navigator(getContext())).navigateTo(intent);
        });
    }

    private void animateWiggle() {
        final Animation animShake = AnimationUtils.loadAnimation(getActivity(), R.anim.wiggle);
        for (int i = 0; i < inputs.length; i++) {
            inputs[i].startAnimation(animShake);
        }
        for (int i = 0; i < lines.length; i++) {
            lines[i].startAnimation(animShake);
        }
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(VIBRATE_DURATION, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(VIBRATE_DURATION);
        }
    }
}