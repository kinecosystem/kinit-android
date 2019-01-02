package org.kinecosystem.tippic.util;

import java.text.SimpleDateFormat;
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

    public static String secondsToFormat(long time, String pattern) {
        Locale local = Locale.getDefault();
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, local);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time * 1000);
        return formatter.format(calendar.getTime());
    }

    public static boolean diffMoreThanADay(long time) {
        Long currentTime = System.currentTimeMillis();
        Long millisecondsAtTimeMidnight = millisAtNextMidnight(time * 1000);
        return currentTime > millisecondsAtTimeMidnight;
    }

    public static String secondsToHHMM(long time) {
        return secondsToFormat(time, "HH:mm");
    }

    public static String secondsToDayMMM(long time) {
        return secondsToFormat(time, "d MMM");
    }
}
