package org.kinecosystem.kinit.view.phoneVerify;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import org.kinecosystem.kinit.KinitApplication;
import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.analytics.Analytics;
import org.kinecosystem.kinit.analytics.Events;
import org.kinecosystem.kinit.navigation.Navigator;
import org.kinecosystem.kinit.repository.UserRepository;
import org.kinecosystem.kinit.server.OperationCompletionCallback;
import org.kinecosystem.kinit.util.GeneralUtils;
import org.kinecosystem.kinit.view.BaseActivity;
import org.kinecosystem.kinit.viewmodel.PhoneVerificationViewModel;

import javax.inject.Inject;

public class PhoneVerifyActivity extends BaseActivity implements PhoneVerificationUIActions {

    public static final String TAG = PhoneVerifyActivity.class.getSimpleName();
    public static final String FRAGMENT_CODE_TAG = "FRAGMENT_CODE_TAG";
    protected static final String PHONE_AUTH_DATA_STORE = "PHONE_AUTH_DATA_STORE";
    private static final String HAS_PREVIOUS = "HAS_PREVIOUS";
    private static final int MAX_SEND_PHONE_COUNT = 3;

    @Inject
    UserRepository userRepository;
    @Inject
    Analytics analytics;
    private PhoneVerificationViewModel model;
    private boolean hasPreviousScreen;
    private int sendPhoneCount = 0;


    public static Intent getIntent(Context context) {
        return new Intent(context, PhoneVerifyActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        KinitApplication.coreComponent.inject(this);
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.phone_veriy_activity);
        hasPreviousScreen = getIntent().getBooleanExtra(HAS_PREVIOUS, false);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, PhoneSendFragment.newInstance(hasPreviousScreen))
                .commit();
        model = new PhoneVerificationViewModel(this, new OperationCompletionCallback() {
            @Override
            public void onError(int error) {
                CodeVerificationFragment codeVerificationFragment = (CodeVerificationFragment) getSupportFragmentManager()
                        .findFragmentByTag(FRAGMENT_CODE_TAG);
                if (codeVerificationFragment != null && codeVerificationFragment.isVisible()) {
                    codeVerificationFragment.onError();
                }
            }

            @Override
            public void onSuccess() {
                userRepository.setPhoneVerified(true);
                analytics.logEvent(new Events.Business.UserVerified());
                GeneralUtils.closeKeyboard(PhoneVerifyActivity.this, findViewById(R.id.fragment_container));
                Navigator navigator = new Navigator(PhoneVerifyActivity.this);
                navigator.navigateTo(Navigator.Destination.WALLET_CREATION);
                PhoneVerifyActivity.this.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        model.removeListener();
    }

    @Override
    public void onSendPhone(String phoneNumber) {
        sendPhoneCount++;
        if (model.startPhoneNumberVerification(phoneNumber)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, CodeVerificationFragment.newInstance(phoneNumber, sendPhoneCount >= MAX_SEND_PHONE_COUNT), FRAGMENT_CODE_TAG)
                    .commit();
        } else {
            showBlackListDialog();
        }
    }

    private void showBlackListDialog() {
        new AlertDialog.Builder(this, R.style.CustomAlertDialog).setTitle(getResources().getString(R.string.oh_no)).setMessage(getResources().getString(R.string.block_area_code_message)).setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                }).create().show();
    }

    @Override
    public void onSendCode(String code) {
        model.verifyPhoneNumberWithCode(code);
    }


    @Override
    public void onBackPressed() {
        onBackPressed(0);
    }

    @Override
    public void onBackPressed(int fromPage) {
        if (fromPage == 0) {
            Navigator navigator = new Navigator(PhoneVerifyActivity.this);
            navigator.navigateTo(Navigator.Destination.TUTORIAL);
            PhoneVerifyActivity.this.finish();
        } else if (fromPage == 1) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, PhoneSendFragment.newInstance(hasPreviousScreen))
                    .commit();
        }
    }
}
