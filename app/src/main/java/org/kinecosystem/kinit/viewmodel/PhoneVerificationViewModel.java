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
import java.util.concurrent.TimeUnit;
import org.kinecosystem.kinit.CoreComponentsProvider;
import org.kinecosystem.kinit.network.OperationCompletionCallback;

public class PhoneVerificationViewModel {

    public static final int PHONE_VERIF_ERROR_WRONG_CODE = 200;
    public static final int PHONE_VERIF_ERROR_OTHER = 201;
    public static final int PHONE_VERIF_ERROR_INVALID_PHONE_CREDENTIALS = 202;
    public static final int PHONE_VERIF_ERROR_SMS_QUOTA_FOR_PROJECT_EXCEEDED = 203;
    public static final int PHONE_VERIF_ERROR_VERIFICATION_FAILED = 204;
    public static final int PHONE_VERIF_ERROR_CANT_GET_FIREBASE_AUTH_TOKEN = 205;

    public static final String TAG = PhoneVerificationViewModel.class.getSimpleName();
    private CoreComponentsProvider coreComponents;
    private OperationCompletionCallback verificationCallback;

    private FirebaseAuth auth;
    private String phoneNumber;

    private String verificationId = "";
    private Activity activity;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                verificationCallback.onError(PHONE_VERIF_ERROR_INVALID_PHONE_CREDENTIALS);
            } else if (e instanceof FirebaseTooManyRequestsException) {
                verificationCallback.onError(PHONE_VERIF_ERROR_SMS_QUOTA_FOR_PROJECT_EXCEEDED);
            }
            verificationCallback.onError(PHONE_VERIF_ERROR_VERIFICATION_FAILED);
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


    public PhoneVerificationViewModel(Activity activity, CoreComponentsProvider coreComponentsProvider,
        OperationCompletionCallback actions) {
        coreComponents = coreComponentsProvider;
        this.activity = activity;
        auth = FirebaseAuth.getInstance();
        this.verificationCallback = actions;
    }

    public void startPhoneNumberVerification(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,        // Phone number to verify
            60,                 // Timeout duration
            TimeUnit.SECONDS,   // Unit of timeout
            activity,               // Activity (for callback binding)
            callbacks);        // OnVerificationStateChangedCallbacks
    }

    public void verifyPhoneNumberWithCode(String code) {
        if (!verificationId.isEmpty()) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithPhoneAuthCredential(credential);
        } else {
            verificationCallback.onError(PHONE_VERIF_ERROR_WRONG_CODE);
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity, task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = task.getResult().getUser();
                    user.getIdToken(true).addOnCompleteListener(tokenRequest -> {
                        if (tokenRequest.isComplete()) {
                            coreComponents.services().getOnBoardingService()
                                .sendPhoneAuthentication(phoneNumber, tokenRequest.getResult().getToken(),
                                    verificationCallback);
                        } else {
                            verificationCallback.onError(PHONE_VERIF_ERROR_CANT_GET_FIREBASE_AUTH_TOKEN);
                        }
                    });
                } else {
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        verificationCallback.onError(PHONE_VERIF_ERROR_WRONG_CODE);
                    } else {
                        Log.e(TAG, "Can't sign in with phone, exception " + task.getException() + ", message " + task
                            .getException().getMessage());
                        verificationCallback.onError(PHONE_VERIF_ERROR_OTHER);
                    }
                }
            });
    }
}
