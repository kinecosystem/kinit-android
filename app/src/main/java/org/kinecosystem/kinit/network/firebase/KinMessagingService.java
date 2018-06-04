package org.kinecosystem.kinit.network.firebase;

import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.kinecosystem.kinit.CoreComponentsProvider;
import org.kinecosystem.kinit.model.Push;
import org.kinecosystem.kinit.model.Push.AuthTokenMessage;
import org.kinecosystem.kinit.model.Push.NotificationMessage;

import java.util.Map;

import static org.kinecosystem.kinit.model.Push.TransactionCompleteMessage;


public class KinMessagingService extends FirebaseMessagingService {

    public static String TAG = KinMessagingService.class.getSimpleName();

    public KinMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "###From: " + remoteMessage.getFrom());
        Log.d(TAG, "###Message data payload: " + remoteMessage.getData());
        Gson gson = new Gson();
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            CoreComponentsProvider coreComponents = getCoreComponentsProvider();
            Map<String, String> data = remoteMessage.getData();
            try {
                if (data.containsKey(Push.TYPE_DATA_KEY) && data.containsKey(Push.MESSAGE_DATA_KEY)) {
                    String type = data.get(Push.TYPE_DATA_KEY);
                    String message = data.get(Push.MESSAGE_DATA_KEY);

                    if (type.equals(Push.TYPE_TX_COMPLETED)) {
                        coreComponents.scheduler().post(() ->
                            coreComponents.services().getWalletService()
                                .onTransactionMessageReceived(gson.fromJson(message, TransactionCompleteMessage.class))
                        );
                    } else if (type.equals(Push.TYPE_AUTH_TOKEN)) {
                        AuthTokenMessage authTokenMessage = gson.fromJson(message, AuthTokenMessage.class);
                        if (!TextUtils.isEmpty(authTokenMessage.getAuthToken())) {
                            coreComponents.services().getOnBoardingService()
                                .sendAuthTokenAck(authTokenMessage.getAuthToken());
                        }
                    } else {
                        // if the json contains message with body field, then this is an engagement push
                        // and we need to send notification to users
                        String id = data.get(Push.ID_DATA_KEY);
                        NotificationMessage notificationMessage = gson.fromJson(message, NotificationMessage.class);
                        if (!TextUtils.isEmpty(id) && notificationMessage != null && !TextUtils
                            .isEmpty(notificationMessage.getBody())) {
                            coreComponents.services().getWalletService().updateBalance(null);
                            coreComponents.services().getWalletService().retrieveTransactions(null);
                            coreComponents.notificationPublisher().notify(id, notificationMessage);
                        }
                    }
                }
            } catch (JsonSyntaxException e) {
                Log.e(TAG, "error json content not valid " + data);
            }

        }

    }

    private CoreComponentsProvider getCoreComponentsProvider() {
        return (CoreComponentsProvider) getApplication();
    }

}
