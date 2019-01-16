package org.kinecosystem.kinit.viewmodel.spend;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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

    public interface AccountInfoResponseListener {
        void onCancel();

        void onError(String error);

        void onAddressReceived(String data);
    }

    boolean startTransferRequestActivity(@NonNull Activity activity, @NonNull String pkg, @NonNull String launchActivityFullPath) {
        Intent intent = createTransferRequestIntent(activity, pkg, launchActivityFullPath);
        if (intent != null) {
            activity.startActivityForResult(intent, REQUEST_CODE);
            return true;
        }
        return false;
    }

    void parseActivityResult(Context context, int requestCode, int resultCode, Intent intent, AccountInfoResponseListener accountInfoResponseListener) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                parseCancel(intent, accountInfoResponseListener);
            } else if (resultCode == Activity.RESULT_OK) {
                parseData(context, intent, accountInfoResponseListener);
            }
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


    private void parseCancel(Intent intent, AccountInfoResponseListener accountInfoResponseListener) {
        if (intent != null) {
            if (intent.hasExtra(EXTRA_HAS_ERROR) && intent.getBooleanExtra(EXTRA_HAS_ERROR, false)) {
                accountInfoResponseListener.onError("");
            } else {
                accountInfoResponseListener.onCancel();
            }
        } else {
            accountInfoResponseListener.onCancel();
        }
    }

    private void parseData(Context context, Intent intent, AccountInfoResponseListener accountInfoResponseListener) {
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
                        accountInfoResponseListener.onAddressReceived(address);

                    } else {
                        accountInfoResponseListener.onError("unable to retrieve public address, input stream contained no data");
                    }
                } else {
                    accountInfoResponseListener.onError("cant create input stream from " + uri);
                }
            } catch (Exception e) {
                accountInfoResponseListener.onError(e.getMessage());
            }
        }

    }

}
