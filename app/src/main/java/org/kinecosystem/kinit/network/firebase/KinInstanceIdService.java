package org.kinecosystem.kinit.network.firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import org.kinecosystem.kinit.CoreComponentsProvider;


public class KinInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (refreshedToken != null) {
            CoreComponentsProvider componentsProvider = (CoreComponentsProvider) getApplicationContext();
            componentsProvider.services().getOnboardingService().updateToken(refreshedToken);
        }
    }
}
