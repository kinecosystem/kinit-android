package org.kinecosystem.kinit.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log

import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.device.DeviceUtils
import org.kinecosystem.kinit.repository.UserRepository

object SupportUtil {

    fun openEmailSupport(context: Context, userRepository: UserRepository? = null) {
        var versionName = ""
        try {
            versionName = context.packageManager
                    .getPackageInfo(context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("SupportUtil", "cant get version name " + e.message)
        }

        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", context.resources.getString(R.string.support_email_address), null))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.resources.getString(R.string.support_email_subject))
        val message = context.resources
                .getString(R.string.support_email_body_template, userRepository?.userInfo?.userId,
                        "android: " + DeviceUtils(context).deviceName(), versionName)
        emailIntent.putExtra(Intent.EXTRA_TEXT, message)
        context.startActivity(emailIntent)
    }
}
