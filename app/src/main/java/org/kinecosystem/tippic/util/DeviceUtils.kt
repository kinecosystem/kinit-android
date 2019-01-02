package org.kinecosystem.tippic.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.content.ContextCompat
import android.telephony.TelephonyManager
import org.kinecosystem.tippic.R
import org.kinecosystem.tippic.analytics.Analytics
import java.util.*


class DeviceUtils {

    companion object {
        fun getLocalDialPrefix(context: Context): String {
            val countryCode = getCountryCode(context.applicationContext)
            var dialPrefix = ""
            if (!countryCode.isEmpty()) {
                val rl = context.resources.getStringArray(R.array.dialCountryPrefixCodes)
                for (i in rl.indices) {
                    val g = rl[i].split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (g[1].trim { it <= ' ' } == countryCode) {
                        dialPrefix = g[0]
                        break
                    }
                }
            }
            return if (dialPrefix.isEmpty()) {
                ""
            } else "+$dialPrefix"
        }

        fun getCountryCode(context: Context): String {
            var countryCode = ""
            val telephonyManager = context.applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (telephonyManager != null) {
                countryCode = telephonyManager.simCountryIso.toUpperCase()
            }
            return countryCode
        }

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

        fun deviceId(context: Context): String {
            var deviceId: String? = null
            if (ContextCompat.checkSelfPermission(context.applicationContext,
                            Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                val telephonyManager: TelephonyManager = context.applicationContext.getSystemService(
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


}