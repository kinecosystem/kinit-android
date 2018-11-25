package org.kinecosystem.kinit.analytics;

import android.app.Application;
import android.util.Log;
import android.view.View;

import com.amplitude.api.Amplitude;
import com.amplitude.api.Identify;
import com.crashlytics.android.Crashlytics;
import com.testfairy.TestFairy;

import org.json.JSONObject;
import org.kinecosystem.kinit.BuildConfig;
import org.kinecosystem.kinit.analytics.Events.Event;

public class Analytics {

    public static final String VIEW_ERROR_TYPE_ONBOARDING = "Onboarding";
    public static final String VIEW_ERROR_TYPE_REWARD = "Reward";
    public static final String VIEW_ERROR_TYPE_TASK_SUBMISSION = "Task Submission";
    public static final String VIEW_ERROR_TYPE_OFFER_NOT_AVAILABLE = "Offer not available";
    public static final String VIEW_ERROR_TYPE_CODE_NOT_PROVIDED = "Code not provided";
    public static final String VIEW_ERROR_TYPE_INTERNET_CONNECTION = "Internet Connection";
    public static final String VIEW_ERROR_TYPE_GENERIC = "Generic";
    public static final String MENU_ITEM_NAME_USE_KIN = "use kin";
    public static final String MENU_ITEM_NAME_EARN = "earn";
    public static final String TRANSACTION_TYPE_P2P = "p2p";
    public static final String TRANSACTION_TYPE_EARN = "earn";
    public static final String TRANSACTION_TYPE_SPEND = "spend";
    public static final String P2P_EXCEED_MIN_MAX = "Exceed max/min Kin";
    public static final String P2P_FRIEND_HAS_NO_ADDRESS = "Friend not exists";
    public static final String P2P_SEND_KIN_TO_SELF = "Send Kin to self";
    public static final String P2P_NOT_ENOUGH_BALANCE = "Exceed existing Kin";
    public static final String SERVER_ERROR_RESPONSE = "server Error response";
    public static final String SERVER_EMPTY_RESPONSE = "server empty response";

    private static final String AMPLITUDE_API_KEY = BuildConfig.AnalyticsApiSecret;
    private static final String TEST_FAIRY_KEY = BuildConfig.TestFairyApiSecret;

    public void init(Application app, boolean isFreshInstall) {
        Amplitude.getInstance().initialize(app, AMPLITUDE_API_KEY)
            .enableForegroundTracking(app);
        if (isFreshInstall) {
            initUserProperties();
        }
        TestFairy.begin(app.getApplicationContext(), TEST_FAIRY_KEY);
    }

    public void addDebugLog(String key, String value) {
        TestFairy.setAttribute(key, value);
    }

    public void setUserId(String userId) {
        Amplitude.getInstance().setUserId(userId);
        Crashlytics.setUserIdentifier(userId);
        TestFairy.setUserId(userId);
    }

    public void protectView(View view) {
        TestFairy.hideView(view);
    }

    public void logEvent(Event event) {
        JSONObject properties = event.getProperties();
        Log.d("Analytics", "logEvent: " + event.getName() + ", properties: " + properties);
        if (properties == null) {
            Amplitude.getInstance().logEvent(event.getName());
        } else {
            Amplitude.getInstance().logEvent(event.getName(), event.getProperties());
        }
    }

    public void setUserProperty(String key, double value) {
        identify(new Identify().set(key, value));
    }

    public void incrementUserProperty(String key, long value) {
        identify(new Identify().add(key, value));
    }

    private void identify(Identify identify) {
        Amplitude.getInstance().identify(identify);
    }

    private void initUserProperties() {
        setUserProperty(Events.UserProperties.BALANCE, 0.0);
        setUserProperty(Events.UserProperties.TRANSACTION_COUNT, 0.0);
        setUserProperty(Events.UserProperties.EARN_COUNT, 0.0);
        setUserProperty(Events.UserProperties.SPEND_COUNT, 0.0);
        setUserProperty(Events.UserProperties.TOTAL_KIN_EARNED, 0.0);
        setUserProperty(Events.UserProperties.TOTAL_KIN_SPENT, 0.0);
    }
}

