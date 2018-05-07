package org.kinecosystem.kinit.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import org.kinecosystem.kinit.R;

public class PhoneUtils {

    public static String getLocalDialPrefix(Context context) {
        String countryId;
        String dialPrefix = "";
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (manager != null) {
            //getNetworkCountryIso
            countryId = manager.getSimCountryIso().toUpperCase();
            String[] rl = context.getResources().getStringArray(R.array.dialCountryPrefixCodes);
            for (int i = 0; i < rl.length; i++) {
                String[] g = rl[i].split(",");
                if (g[1].trim().equals(countryId.trim())) {
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
}
