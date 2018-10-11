package org.kinecosystem.kinit.viewmodel;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.kinecosystem.kinit.KinitApplication;
import org.kinecosystem.kinit.navigation.Navigator;
import org.kinecosystem.kinit.repository.UserRepository;
import org.kinecosystem.kinit.server.NetworkServices;
import org.kinecosystem.kinit.server.OperationCompletionCallback;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class PhoneVerificationViewModel {

    public static final int PHONE_VERIF_ERROR_WRONG_CODE = 200;
    public static final int PHONE_VERIF_ERROR_OTHER = 201;
    public static final int PHONE_VERIF_ERROR_INVALID_PHONE_CREDENTIALS = 202;
    public static final int PHONE_VERIF_ERROR_SMS_QUOTA_FOR_PROJECT_EXCEEDED = 203;
    public static final int PHONE_VERIF_ERROR_VERIFICATION_FAILED = 204;
    public static final int PHONE_VERIF_ERROR_CANT_GET_FIREBASE_AUTH_TOKEN = 205;

    public static final String TAG = PhoneVerificationViewModel.class.getSimpleName();
    @Inject
    NetworkServices servicesProvider;
    @Inject
    UserRepository userRepository;
    private OperationCompletionCallback verificationCallback;

    private FirebaseAuth auth;

    private String verificationId = "";
    private Activity activity;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            if (verificationCallback != null) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    verificationCallback.onError(PHONE_VERIF_ERROR_INVALID_PHONE_CREDENTIALS);
                    Log.e(TAG, "invalid code FirebaseAuthInvalidCredentialsException");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    verificationCallback.onError(PHONE_VERIF_ERROR_SMS_QUOTA_FOR_PROJECT_EXCEEDED);
                    Log.e(TAG, "reached sms quota limit FirebaseTooManyRequestsException");
                }
                verificationCallback.onError(PHONE_VERIF_ERROR_VERIFICATION_FAILED);
                Log.e(TAG, "Exception occurred while verifying code " + e.getMessage());
            }
        }

        @Override
        public void onCodeSent(String verificationId,
            PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            PhoneVerificationViewModel.this.verificationId = verificationId;
        }
    };

    public PhoneVerificationViewModel(Activity activity,
        OperationCompletionCallback actions) {
        KinitApplication.coreComponent.inject(this);
        this.activity = activity;
        auth = FirebaseAuth.getInstance();
        this.verificationCallback = actions;
    }

    public void removeListener() {
        this.verificationCallback = null;
    }

    public boolean startPhoneNumberVerification(String phoneNumber) {
        if (servicesProvider.getOnBoardingService().isValidNumber(phoneNumber)) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    activity,               // Activity (for listener binding)
                    callbacks);        // OnVerificationStateChangedCallbacks
            return true;
        }
        return false;
    }

    public void verifyPhoneNumberWithCode(String code) {
        if (!verificationId.isEmpty()) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithPhoneAuthCredential(credential);
        } else {
            if (verificationCallback != null) {
                verificationCallback.onError(PHONE_VERIF_ERROR_WRONG_CODE);
            }
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity, task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = task.getResult().getUser();
                    user.getIdToken(true).addOnCompleteListener(tokenRequest -> {
                        if (tokenRequest.isComplete() && verificationCallback != null) {
                            servicesProvider.getOnBoardingService()
                                .sendAuthentication(tokenRequest.getResult().getToken(),
                                    verificationCallback);
                        } else {
                            verificationCallback.onError(PHONE_VERIF_ERROR_CANT_GET_FIREBASE_AUTH_TOKEN);
                        }
                    });
                } else {
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException && verificationCallback != null) {
                        verificationCallback.onError(PHONE_VERIF_ERROR_WRONG_CODE);
                    } else {
                        Log.e(TAG, "Can't sign in with phone, exception " + task.getException() + ", message " + task
                            .getException().getMessage());
                        if (verificationCallback != null) {
                            verificationCallback.onError(PHONE_VERIF_ERROR_OTHER);
                        }
                    }
                }
            });
    }

    public Navigator.Destination nextActivity() {
        if (!userRepository.getRestoreHints().isEmpty()) {
            return Navigator.Destination.WALLET_RESTORE;
        }
        return Navigator.Destination.WALLET_CREATE;
    }
}
