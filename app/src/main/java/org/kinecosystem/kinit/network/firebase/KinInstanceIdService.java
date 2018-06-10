package org.kinecosystem.kinit.network.firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import javax.inject.Inject;
import org.kinecosystem.kinit.KinitApplication;
import org.kinecosystem.kinit.network.OnboardingService;


public class KinInstanceIdService extends FirebaseInstanceIdService {

    @Inject
    OnboardingService onboardingService;

    @Override
    public void onTokenRefresh() {
        KinitApplication.getCoreComponent().inject(this);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (refreshedToken != null) {
            onboardingService.updateToken(refreshedToken);
        }
    }
}
