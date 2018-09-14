package org.kinecosystem.kinit.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.util.Log

import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.repository.UserRepository

object SupportUtil {

    fun openEmailFeedback(context: Context, userRepository: UserRepository) {
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", context.resources.getString(R.string.feedback_email_address), null))
        if (context.packageManager != null && emailIntent.resolveActivity(context.packageManager) != null) {
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.resources.getString(R.string.feedback_email_subject))
            val message = context.resources.getString(R.string.feedback_email_body_template)
            emailIntent.putExtra(Intent.EXTRA_TEXT, message)
            context.startActivity(emailIntent)
        } else {
            showSupportDialog(context, userRepository.userId())
        }
    }

    fun openEmailSupport(context: Context, userRepository: UserRepository) {
        var versionName = ""
        try {
            versionName = context.packageManager
                    .getPackageInfo(context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("SupportUtil", "cant get version name " + e.message)
        }

        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", context.resources.getString(R.string.support_email_address), null))
        if (context.packageManager != null && emailIntent.resolveActivity(context.packageManager) != null) {
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.resources.getString(R.string.support_email_subject))
            val message = context.resources
                    .getString(R.string.support_email_body_template, userRepository.userId(),
                            "android: " + DeviceUtils.deviceName(), versionName)
            emailIntent.putExtra(Intent.EXTRA_TEXT, message)
            context.startActivity(emailIntent)
        } else {
            showSupportDialog(context, userRepository.userId())
        }
    }

    private fun showSupportDialog(context: Context, userId: String) {
        val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
        var alertDialog: AlertDialog? = null
        builder.setTitle(context.resources.getString(R.string.contact_support))
            .setMessage(context.resources.getString(R.string.support_email_no_mail_app, userId))
            .setPositiveButton(context.resources.getString(R.string.dialog_ok)) { _, _ -> alertDialog?.dismiss() }
        alertDialog = builder.create()
        alertDialog.show()
    }
}
