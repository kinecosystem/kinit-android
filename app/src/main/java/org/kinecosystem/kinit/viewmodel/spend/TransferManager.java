package org.kinecosystem.kinit.viewmodel.spend;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class TransferManager {
    private final int REQUEST_CODE = 77;
    private final String EXTRA_SOURCE_APP_NAME = "EXTRA_SOURCE_APP_NAME";
    private final String EXTRA_HAS_ERROR = "EXTRA_HAS_ERROR";

    public interface OnAccountInfoResponse {
        void onCancel();

        void onError(String error);

        void onAddressRecieved(String data);
    }

    boolean startTransferRequestActivity(@NonNull Activity activity, @NonNull String pkg, @NonNull String launchActivityFullPath) {
        Intent intent = createTransferRequestIntent(activity, pkg, launchActivityFullPath);
        if (intent != null) {
            activity.startActivityForResult(intent, REQUEST_CODE);
            return true;
        }
        return false;
    }

    void parseActivityResult(Context context, int requestCode, int resultCode, Intent intent, OnAccountInfoResponse onAccountInfoResponse) {
        if (resultCode == Activity.RESULT_CANCELED) {
            parseCancel(intent, onAccountInfoResponse);
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            parseData(context, intent, onAccountInfoResponse);
        }
    }


    private Intent createTransferRequestIntent(@NonNull Context context, @NonNull String pkg, @NonNull String launchActivityFullPath) {
        Intent intent = new Intent();
        intent.setPackage(pkg);
        intent.setComponent(new ComponentName(pkg, launchActivityFullPath));
        String appName = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
        intent.putExtra(EXTRA_SOURCE_APP_NAME, appName);
        final List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, 0);
        if (!resolveInfos.isEmpty()) {
            return intent;
        }
        return null;
    }


    private void parseCancel(Intent intent, OnAccountInfoResponse onAccountInfoResponse) {
        if (intent != null) {
            if (intent.hasExtra(EXTRA_HAS_ERROR) && intent.getBooleanExtra(EXTRA_HAS_ERROR, false)) {
                onAccountInfoResponse.onError("");
            } else {
                onAccountInfoResponse.onCancel();
            }
        } else {
            onAccountInfoResponse.onCancel();
        }
    }

    private void parseData(Context context, Intent intent, OnAccountInfoResponse onAccountInfoResponse) {
        if (intent != null && intent.getData() != null) {
            try {
                Uri uri = intent.getData();
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                if (inputStream != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();

                    String data = reader.readLine();
                    while (data != null) {
                        stringBuilder.append(data).append('\n');
                        data = reader.readLine();
                    }
                    String address = stringBuilder.toString();
                    if (!address.isEmpty()) {
                        Log.d("####", "#### got address  " + address);
                        onAccountInfoResponse.onAddressRecieved(address);

                    } else {
                        Log.d("####", "#### no address empty ");
                        onAccountInfoResponse.onError("got empty data");
                    }
                } else {
                    Log.d("####", "#### inpiutstream error ");
                    onAccountInfoResponse.onError("cant create input stream from " + uri);
                }
            } catch (Exception e) {
                Log.d("####", "#### inpiutstream error " + e.getMessage());
                onAccountInfoResponse.onError(e.getMessage());
            }
        }

    }

}
