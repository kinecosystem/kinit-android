package org.kinecosystem.kinit.view.phoneVerify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.util.GeneralUtils;
import org.kinecosystem.kinit.view.BaseActivity;
import org.kinecosystem.kinit.view.MainActivity;
import org.kinecosystem.kinit.view.tutorial.TutorialActivity;
import org.kinecosystem.kinit.viewmodel.PhoneVerificationViewModel;
import org.kinecosystem.kinit.viewmodel.PhoneVerificationViewModel.phoneAuthModelActions;

public class PhoneVerifyActivity extends BaseActivity implements PhoneVerificationUIActions {

    public static final String TAG = PhoneVerifyActivity.class.getSimpleName();
    public static final String FRAGMENT_CODE_TAG = "FRAGMENT_CODE_TAG";
    protected static final String PHONE_AUTH_DATA_STORE = "PHONE_AUTH_DATA_STORE";
    private static final String HAS_PREVIOUS = "HAS_PREVIOUS";

    public static Intent getIntent(Context context, boolean hasPrevious) {
        Intent intent = new Intent(context, PhoneVerifyActivity.class);
        intent.putExtra(HAS_PREVIOUS, hasPrevious);
        return intent;
    }

    private PhoneVerificationViewModel model;
    private boolean hasPreviousScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        setContentView(R.layout.phone_veriy_activity);
        hasPreviousScreen = getIntent().getBooleanExtra(HAS_PREVIOUS, false);
        getSupportFragmentManager().beginTransaction()
            .add(R.id.fragment_container, PhoneSendFragment.newInstance(hasPreviousScreen))
            .commit();
        model = new PhoneVerificationViewModel(this, getCoreComponents(), new phoneAuthModelActions() {
            @Override
            public void onError(String error) {
                CodeVerificationFragment codeVerificationFragment = (CodeVerificationFragment) getSupportFragmentManager()
                    .findFragmentByTag(FRAGMENT_CODE_TAG);
                if (codeVerificationFragment != null && codeVerificationFragment.isVisible()) {
                    codeVerificationFragment.onError();
                }
            }

            @Override
            public void onWrongCode() {
                CodeVerificationFragment codeVerificationFragment = (CodeVerificationFragment) getSupportFragmentManager()
                    .findFragmentByTag(FRAGMENT_CODE_TAG);
                if (codeVerificationFragment != null && codeVerificationFragment.isVisible()) {
                    codeVerificationFragment.onWrongCode();
                }
            }

            @Override
            public void onAuthComplete() {
                getCoreComponents().userRepo().setPhoneVerified(true);
                getCoreComponents().userRepo().setFirstTimeUser(false);
                GeneralUtils.closeKeyboard(PhoneVerifyActivity.this, findViewById(R.id.fragment_container));
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, PhoneAuthCompleteFragment.newInstance())
                    .commit();
            }
        });
    }

    @Override
    public void onSendPhone(String phoneNumber) {
        model.startPhoneNumberVerification(phoneNumber);
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragment_container, CodeVerificationFragment.newInstance(phoneNumber), FRAGMENT_CODE_TAG)
            .commit();
    }

    @Override
    public void onSendCode(String code) {
        model.verifyPhoneNumberWithCode(code);
    }

    @Override
    public void onVerificationComplete() {
        startActivity(MainActivity.getIntent(this));
        finish();
    }

    @Override
    public void onBackPressed(int fromPage) {
        if (fromPage == 0) {
            startActivity(TutorialActivity.getIntent(this));
            finish();
        } else if (fromPage == 1) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, PhoneSendFragment.newInstance(hasPreviousScreen))
                .commit();
        }
    }
}
