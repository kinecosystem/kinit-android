package org.kinecosystem.kinit.device

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.content.ContextCompat
import android.telephony.TelephonyManager
import org.kinecosystem.kinit.analytics.Analytics
import java.util.*


class DeviceUtils(context: Context) {
    private val applicationContext: Context = context.applicationContext

    fun timeZone(): String {
        val timeDiff = TimeZone.getDefault().getOffset(System.currentTimeMillis()) / 3600000
        val sign = if (timeDiff >= 0) "+" else ""
        return "$sign$timeDiff:00"
    }

    fun timeZoneDebugging(analytics: Analytics) {
        val defaultTimeZone = TimeZone.getDefault()
        analytics.addDebugLog("timeZone() method ", timeZone())
        analytics.addDebugLog("Locale Country",
            Locale.getDefault().country + ":" + Locale.getDefault().getDisplayCountry())
        analytics.addDebugLog("timeZone-DefaultId", defaultTimeZone.id)
        analytics.addDebugLog("timeZone-DisplayShort", defaultTimeZone.getDisplayName(false, TimeZone.SHORT))
        analytics.addDebugLog("timeZone-DisplayLong", TimeZone.getDefault().getDisplayName(false, TimeZone.LONG))
    }

    fun deviceId(): String {
        var deviceId: String? = null
        if (ContextCompat.checkSelfPermission(applicationContext,
            Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            val telephonyManager: TelephonyManager = applicationContext.getSystemService(
                Context.TELEPHONY_SERVICE) as TelephonyManager
            deviceId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                telephonyManager.getImei()
            } else {
                telephonyManager.deviceId
            }
        }
        return deviceId.orEmpty()
    }


    fun deviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            model
        } else {
            "$manufacturer $model"
        }
    }
}