package org.kinecosystem.kinit.util;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import org.kinecosystem.kinit.R;

public class PhoneUtils {

    public static String getLocalDialPrefix(Context context) {
        String countryCode = getCountryCode(context);
        String dialPrefix = "";
        if (!countryCode.isEmpty()) {
            String[] rl = context.getResources().getStringArray(R.array.dialCountryPrefixCodes);
            for (int i = 0; i < rl.length; i++) {
                String[] g = rl[i].split(",");
                if (g[1].trim().equals(countryCode)) {
                    dialPrefix = g[0];
                    break;
                }
            }
        }
        if (dialPrefix.isEmpty()) {
            return "";
        }
        return "+" + dialPrefix;
    }

    public static String getCountryCode(Context context) {
        String countryCode = "";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            countryCode = telephonyManager.getSimCountryIso().toUpperCase();
        }
        return countryCode;
    }
}
