package org.kinecosystem.kinit.view.phoneVerify;

public interface PhoneVerificationUIActions {
    void onSendPhone(String phoneNumber);
    void onSendCode(String code);
    void onVerificationComplete();
    void onBackPressed(int fromPage);
}
