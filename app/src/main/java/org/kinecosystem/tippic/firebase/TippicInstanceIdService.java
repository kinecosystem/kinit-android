package org.kinecosystem.tippic.firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import javax.inject.Inject;
import org.kinecosystem.tippic.TippicApplication;
import org.kinecosystem.tippic.server.OnboardingService;


public class TippicInstanceIdService extends FirebaseInstanceIdService {

    @Inject
    OnboardingService onboardingService;

    @Override
    public void onTokenRefresh() {
        TippicApplication.getCoreComponent().inject(this);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (refreshedToken != null) {
            onboardingService.updateToken(refreshedToken);
        }
    }
}
