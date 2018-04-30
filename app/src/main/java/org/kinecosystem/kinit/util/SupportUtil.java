package org.kinecosystem.kinit.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import org.kinecosystem.kinit.R;
import org.kinecosystem.kinit.device.DeviceUtils;
import org.kinecosystem.kinit.repository.UserRepository;

public class SupportUtil {

    public static void openEmailSupport(Context context, UserRepository userRepository) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
            "mailto", context.getResources().getString(R.string.support_email_address), null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.support_email_subject));
        String message = context.getResources()
            .getString(R.string.support_email_body_template, userRepository.getUserInfo().getUserId(),
                "android: " + new DeviceUtils(context).deviceName());
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        context.startActivity(emailIntent);
    }


}
