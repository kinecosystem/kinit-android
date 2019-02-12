package org.kinecosystem.kinit.viewmodel.spend;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class TransferManager {
    private final int REQUEST_CODE = 77;
    private final String EXTRA_SOURCE_APP_NAME = "EXTRA_SOURCE_APP_NAME";
    private final String EXTRA_HAS_ERROR = "EXTRA_HAS_ERROR";
    private final String EXTRA_ERROR_MESSAGE = "EXTRA_ERROR_MESSAGE";

    public interface AccountInfoResponseListener {
        void onCancel();

        void onError(String error);

        void onAddressReceived(String data);
    }

    /**
     * Checks that the move kin launching activity in the given application exists and if it does,
     * starts it using startActivityForResult
     *
     * @param activity               The source application activity originating the request
     * @param applicationId          The application id of the destination application
     * @param launchActivityFullPath The full path to the activity in the destination app that is responsible for
     *                               returning a result with the destination app public address
     * @return boolean true if the destination activity has been started
     */
    public boolean startTransferRequestActivity(@NonNull Activity activity, @NonNull String applicationId, @NonNull String launchActivityFullPath) {
        PackageManager packageManager = activity.getPackageManager();
        Intent intent = new Intent();
        intent.setPackage(applicationId);
        intent.setComponent(new ComponentName(applicationId, launchActivityFullPath));
        final List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, 0);
        if (!resolveInfos.isEmpty()) {
            final boolean exported = resolveInfos.get(0).activityInfo.exported;
            if (exported) {
                String appName = activity.getApplicationInfo().loadLabel(packageManager).toString();
                intent.putExtra(EXTRA_SOURCE_APP_NAME, appName);
                try {
                    activity.startActivityForResult(intent, REQUEST_CODE);
                } catch (Exception e) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Method should be called from onActivityResult
     *
     * @param context                     The activity context is needed to process the result
     * @param requestCode                 The requestCode received in onActivityResult
     * @param resultCode                  The resultCode received in onActivityResult
     * @param intent                      The intent received in onActivityResult
     * @param accountInfoResponseListener A listener that can handle the response
     */
    public void processResponse(Context context, int requestCode, int resultCode, Intent intent, AccountInfoResponseListener accountInfoResponseListener) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                processResultOk(context, intent, accountInfoResponseListener);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                processResultCanceled(intent, accountInfoResponseListener);
            }
        }
    }


    private void processResultOk(Context context, Intent intent, AccountInfoResponseListener accountInfoResponseListener) {
        try {
            Uri uri = intent.getData();
            InputStream inputStream = context.getContentResolver().openInputStream(uri);

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            String data = reader.readLine();
            while (data != null) {
                stringBuilder.append(data).append('\n');
                data = reader.readLine();
            }
            String address = stringBuilder.toString();
            if (!address.isEmpty()) {
                accountInfoResponseListener.onAddressReceived(address);

            } else {
                accountInfoResponseListener.onError("unable to retrieve public address, input stream contained no data");
            }
        } catch (Exception e) {
            accountInfoResponseListener.onError(e.getMessage());
        }
    }

    private void processResultCanceled(Intent intent, AccountInfoResponseListener accountInfoResponseListener) {
        if (intent != null) {
            if (intent.getBooleanExtra(EXTRA_HAS_ERROR, false)) {
                accountInfoResponseListener.onError(intent.getStringExtra(EXTRA_ERROR_MESSAGE));
            } else {
                accountInfoResponseListener.onCancel();
            }
        } else {
            accountInfoResponseListener.onCancel();
        }
    }
}
