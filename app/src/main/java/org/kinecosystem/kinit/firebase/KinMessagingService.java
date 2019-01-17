package org.kinecosystem.kinit.firebase;

import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.kinecosystem.kinit.KinitApplication;
import org.kinecosystem.kinit.analytics.Analytics;
import org.kinecosystem.kinit.analytics.Events.BILog.AuthTokenAckFailed;
import org.kinecosystem.kinit.analytics.Events.BILog.AuthTokenReceived;
import org.kinecosystem.kinit.model.Push;
import org.kinecosystem.kinit.model.Push.AuthTokenMessage;
import org.kinecosystem.kinit.model.Push.NotificationMessage;
import org.kinecosystem.kinit.notification.NotificationPublisher;
import org.kinecosystem.kinit.repository.UserRepository;
import org.kinecosystem.kinit.server.NetworkServices;
import org.kinecosystem.kinit.util.Scheduler;

import java.util.Map;

import javax.inject.Inject;

import static org.kinecosystem.kinit.model.Push.TransactionCompleteMessage;


public class KinMessagingService extends FirebaseMessagingService {

    public static String TAG = KinMessagingService.class.getSimpleName();

    @Inject
    NotificationPublisher notificationPublisher;
    @Inject
    NetworkServices servicesProvider;
    @Inject
    UserRepository userRepository;
    @Inject
    Scheduler scheduler;
    @Inject
    Analytics analytics;

    public KinMessagingService() {

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        KinitApplication.coreComponent.inject(this);
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        Gson gson = new Gson();
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();

            if (data.containsKey(Push.TYPE_DATA_KEY) && data.containsKey(Push.MESSAGE_DATA_KEY)) {
                String type = data.get(Push.TYPE_DATA_KEY);
                String message = data.get(Push.MESSAGE_DATA_KEY);

                try {

                    if (type.equals(Push.TYPE_TX_COMPLETED)) {
                        scheduler.post(() ->
                            servicesProvider.getWalletService()
                                .onTransactionMessageReceived(
                                    gson.fromJson(message, TransactionCompleteMessage.class))
                        );
                    } else if (type.equals(Push.TYPE_AUTH_TOKEN)) {
                        analytics.logEvent(new AuthTokenReceived());
                        AuthTokenMessage authTokenMessage = gson.fromJson(message, AuthTokenMessage.class);
                        String token = authTokenMessage.getAuthToken();
                        if (!TextUtils.isEmpty(token)) {
                            userRepository.setAuthToken(token);
                            servicesProvider.getOnBoardingService().sendAuthTokenAck(token);
                        } else {
                            analytics.logEvent(new AuthTokenAckFailed("empty auth token" + authTokenMessage));
                        }
                    } else if (type.equals(Push.TYPE_REGISTER)) {
                        servicesProvider.getOnBoardingService().registerOnDemand();
                    } else {
                        // if the json contains message with body field, then this is an engagement push
                        // and we need to send notification to users
                        String id = data.get(Push.ID_DATA_KEY);
                        NotificationMessage notificationMessage = gson.fromJson(message, NotificationMessage.class);
                        if (!TextUtils.isEmpty(id) && notificationMessage != null && !TextUtils
                            .isEmpty(notificationMessage.getBody())) {
                            servicesProvider.getWalletService().updateBalance(null);
                            servicesProvider.getWalletService().retrieveTransactions(null);
                            notificationPublisher.notify(id, notificationMessage);
                        }
                    }

                } catch (JsonSyntaxException e) {
                    Log.e(TAG, "error json content not valid " + data);
                    if (type != null && type.equals(Push.TYPE_AUTH_TOKEN)) {
                        analytics.logEvent(new AuthTokenAckFailed(
                            "JsonException for message=" + message + ", e = " + e.getMessage()));

                    }
                }
            }
        }
    }
}
