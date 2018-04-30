package org.kinecosystem.kinit.view.earn;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import org.kinecosystem.kinit.view.SingleFragmentActivity;

public class TestActivity extends SingleFragmentActivity {


    public static Intent getIntent(Context context) {
        return new Intent(context, TestActivity.class);
    }

    @Override
    protected Fragment getFragment() {
        return QuestionnaireRewardFragment.Companion.newInstance();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
