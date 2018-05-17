package org.kinecosystem.kinit.view.phoneVerify;


import static org.kinecosystem.kinit.view.phoneVerify.PhoneVerifyActivity.PHONE_AUTH_DATA_STORE;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.analytics.Events;
import org.kinecosystem.kinit.repository.DataStore;
import org.kinecosystem.kinit.view.BaseFragment;


public class CodeVerificationFragment extends BaseFragment {

    public static final String TAG = CodeVerificationFragment.class.getSimpleName();
    private static final String KEY_RESEND_CODE = "KEY_RESEND_CODE";
    private static final String PHONE_NUMBER = "PHONE_NUMBER";
    private static final long SEC = 1000;
    private static final long COUNT_DOWN_DURATION = 16 * SEC;
    private static final long CODE_LENGTH = 6;
    private static final long VIBRATE_DURATION = 500;

    private TextView code, next, subtitle, counter, resend;
    private ProgressBar progressBar;
    private PhoneVerificationUIActions actions;
    private CountDownTimer timer = new CountDownTimer(COUNT_DOWN_DURATION, SEC) {
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

    @Override
    public void onResume() {
        super.onResume();
        getCoreComponents().analytics().logEvent(new Events.Analytics.ViewVerificationPage());
    }

    public static CodeVerificationFragment newInstance(String phone) {
        CodeVerificationFragment fragment = new CodeVerificationFragment();
        Bundle args = new Bundle();
        args.putString(PHONE_NUMBER, phone);
        fragment.setArguments(args);
        return fragment;
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
        code = view.findViewById(R.id.code_input);
        code.setFocusable(true);
        code.requestFocus();
        counter = view.findViewById(R.id.counter);
        String counterText = getResources().getString(R.string.code_sms_counter_subtitle, 15);
        counter.setText(counterText);
        final String phoneNumber = getArguments().getString(PHONE_NUMBER);
        subtitle = view.findViewById(R.id.subtitle);
        String subtitleStr = getResources().getString(R.string.verification_code_subtitle, phoneNumber);
        subtitle.setText(subtitleStr);
        resend = view.findViewById(R.id.resend);
        resend.setOnClickListener(view12 -> {
            sendEvent();
            actions.onBackPressed(1);
        });
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
                next.setEnabled(charSequence.length() >= CODE_LENGTH);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        timer.start();

        return view;
    }

    private void sendEvent() {
        DataStore dataStore = getCoreComponents().dataStore(PHONE_AUTH_DATA_STORE);
        int resendCodeCount = dataStore.getInt(KEY_RESEND_CODE, 0);
        resendCodeCount++;
        getCoreComponents().analytics()
            .logEvent(new Events.Analytics.ClickNewCodeLinkOnVerificationPage(resendCodeCount));
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
        code.setText("");
        progressBar.setVisibility(View.GONE);
        next.setEnabled(true);
    }

    private void animateWiggle() {
        final Animation animShake = AnimationUtils.loadAnimation(getActivity(), R.anim.wiggle);
        animShake.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                onEndWiggleAnim();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        code.startAnimation(animShake);
    }

    void onEndWiggleAnim() {
        code.setText("");
        next.setEnabled(true);
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(VIBRATE_DURATION, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(VIBRATE_DURATION);
        }
    }

    public void onWrongCode() {
        getCoreComponents().analytics()
            .logEvent(new Events.Analytics.ViewErrorMessageOnVerificationPage());
        progressBar.setVisibility(View.GONE);
        vibrate();
        animateWiggle();
    }
}
