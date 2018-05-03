package org.kinecosystem.kinit.view.phoneVerify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.view.BaseActivity;

public class PhoneVerifyActivity extends BaseActivity {


    public static Intent getIntent(Context context) {
        return new Intent(context, PhoneVerifyActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.phone_veriy_activity);
        getCoreComponents().userRepo().setPhoneVerified(true);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

}
