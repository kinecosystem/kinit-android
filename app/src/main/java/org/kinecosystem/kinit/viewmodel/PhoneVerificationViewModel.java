package org.kinecosystem.kinit.viewmodel;

import android.app.Activity;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;
import org.kinecosystem.kinit.CoreComponentsProvider;
import org.kinecosystem.kinit.network.OnboardingApi.StatusResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneVerificationViewModel {

    public interface phoneAuthModelActions {

        void onError(String error);
        void onWrongCode();
        void onAuthComplete();
    }

    public static final String TAG = PhoneVerificationViewModel.class.getSimpleName();
    private CoreComponentsProvider coreComponents;
    private phoneAuthModelActions actions;

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
                actions.onError("Invalid phone number - Invalid Credentials Exception");
            } else if (e instanceof FirebaseTooManyRequestsException) {
                actions.onError("verification failed - The SMS quota for the project has been exceeded");
            }
            actions.onError("verification failed");
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
        phoneAuthModelActions actions) {
        coreComponents = coreComponentsProvider;
        this.activity = activity;
        auth = FirebaseAuth.getInstance();
        this.actions = actions;
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
            actions.onWrongCode();
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
                                    new Callback<StatusResponse>() {
                                        @Override
                                        public void onResponse(Call<StatusResponse> call,
                                            Response<StatusResponse> response) {
                                            if (response != null && response.isSuccessful()) {
                                                actions.onAuthComplete();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<StatusResponse> call, Throwable t) {
                                            actions.onError("Cant sendPhoneAuthentication" + t.getMessage());
                                        }
                                    });
                        }else{
                            actions.onError("Cant get token from user by firebase");
                        }
                    });
                } else {
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        actions.onWrongCode();
                    } else {
                        actions.onError("Cant sign in with phone");
                    }
                }
            });
    }
}
