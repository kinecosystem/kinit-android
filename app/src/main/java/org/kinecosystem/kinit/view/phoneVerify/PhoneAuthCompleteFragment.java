package org.kinecosystem.kinit.view.phoneVerify;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.analytics.Events;
import org.kinecosystem.kinit.view.BaseFragment;


public class PhoneAuthCompleteFragment extends BaseFragment {

    public static final String TAG = PhoneAuthCompleteFragment.class.getSimpleName();
    public static final long TIMEOUT = 5000;
    private PhoneVerificationUIActions actions;
    private KonfettiView konfettiView;

    public static PhoneAuthCompleteFragment newInstance() {
        PhoneAuthCompleteFragment fragment = new PhoneAuthCompleteFragment();
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
        konfettiView = view.findViewById(R.id.view_konfetti);
        konfettiView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (konfettiView.getWidth() > 0) {
                    konfettiView.getViewTreeObserver().removeOnGlobalLayoutListener(this::onGlobalLayout);
                }
                startKonfettiAnim();
            }
        });

        return view;
    }

    private void startKonfettiAnim() {
        konfettiView.build()
            .addColors(getResources().getColor(R.color.konetti1), getResources().getColor(R.color.konetti2),
                getResources().getColor(R.color.konetti3), getResources().getColor(R.color.konetti4))
            .setDirection(0.0, 359.0)
            .setSpeed(0f, 8f)
            .setFadeOutEnabled(true)
            .setTimeToLive(1000L)
            .addShapes(Shape.RECT, Shape.CIRCLE)
            .addSizes(new Size(10, 5))
            .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
            .stream(200, 5000L);
    }

    @Override
    public void onResume() {
        super.onResume();
        getCoreComponents().analytics().logEvent(new Events.Analytics.ViewOnboardingCompletedPage());
    }
}
