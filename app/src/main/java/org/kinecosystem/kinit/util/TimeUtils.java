package org.kinecosystem.kinit.util;
import java.util.Calendar;
import java.util.Locale;

public class TimeUtils {
    public static long millisAtNextMidnight(long currentTime) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(currentTime);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        return cal.getTimeInMillis();
    }

}
