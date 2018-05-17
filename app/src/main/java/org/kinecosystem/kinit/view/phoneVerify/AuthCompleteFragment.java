package org.kinecosystem.kinit.view.phoneVerify;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.analytics.Events;
import org.kinecosystem.kinit.view.BaseFragment;


public class AuthCompleteFragment extends BaseFragment {

    public static final String TAG = AuthCompleteFragment.class.getSimpleName();
    public static final long TIMEOUT = 4000;
    private PhoneVerificationUIActions actions;


    public static AuthCompleteFragment newInstance() {
        AuthCompleteFragment fragment = new AuthCompleteFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.phone_auth_complete_fragment, container, false);
        if (getActivity() instanceof PhoneVerificationUIActions) {
            actions = (PhoneVerificationUIActions) getActivity();
        } else {
            Log.e(TAG, "activity must implements PhoneVerificationActions");
            getActivity().finish();
        }
        view.postDelayed(() -> actions.onVerificationComplete(), TIMEOUT);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getCoreComponents().analytics().logEvent(new Events.Analytics.ViewOnboardingCompletedPage());
    }
}
