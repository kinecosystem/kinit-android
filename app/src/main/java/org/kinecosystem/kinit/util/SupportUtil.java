package org.kinecosystem.kinit.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.device.DeviceUtils;
import org.kinecosystem.kinit.repository.UserRepository;

public class SupportUtil {

    public static void openEmailSupport(Context context, UserRepository userRepository) {
        String versionName = "";
        try {
            versionName = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("SupportUtil", "cant get version name " + e.getMessage());
        }

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", context.getResources().getString(R.string.support_email_address), null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.support_email_subject));
        String message = context.getResources()
                .getString(R.string.support_email_body_template, userRepository.getUserInfo().getUserId(),
                        "android: " + new DeviceUtils(context).deviceName(), versionName);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        context.startActivity(emailIntent);
    }

}
