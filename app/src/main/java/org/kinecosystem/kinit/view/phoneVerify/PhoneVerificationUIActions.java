package org.kinecosystem.kinit.view.phoneVerify;

import org.kinecosystem.ClientValidator;

public interface PhoneVerificationUIActions {

    void onSendPhone(String phoneNumber);

    void onSendCode(String code);

    void onBackPressed(int fromPage);

    ClientValidator getValidateClient();
}
