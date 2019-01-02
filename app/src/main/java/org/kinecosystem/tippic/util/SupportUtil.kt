package org.kinecosystem.tippic.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.util.Log
import org.kinecosystem.tippic.R
import org.kinecosystem.tippic.repository.UserRepository

object SupportUtil {

    enum class Type {
        SUPPORT,
        FEEDBACK
    }

    fun openEmail(context: Context, userRepository: UserRepository, type: Type) {
        var address = ""
        var subject = ""
        var message = ""
        var noMailAppTitle = ""
        var noMailAppBody = ""
        var versionName = ""
        try {
            versionName = context.packageManager
                    .getPackageInfo(context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("SupportUtil", "cant get version name " + e.message)
        }

        when (type) {
            Type.SUPPORT -> {
                address = context.resources.getString(R.string.support_email_address)
                subject = context.resources.getString(R.string.support_email_subject)
                message = context.resources.getString(R.string.support_email_body_template, userRepository.userId(),
                        "android: " + DeviceUtils.deviceName(), versionName)
                noMailAppTitle = context.resources.getString(R.string.contact_support)
                noMailAppBody = context.resources.getString(R.string.support_email_no_mail_app, userRepository.userId())
            }
            Type.FEEDBACK -> {
                address = context.resources.getString(R.string.feedback_email_address)
                subject = context.resources.getString(R.string.feedback_email_subject)
                message = context.resources.getString(R.string.feedback_email_body_template, versionName)
                noMailAppTitle = context.resources.getString(R.string.send_feedback)
                noMailAppBody = context.resources.getString(R.string.feedback_email_no_mail_app)
            }
        }

        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", address, null))
        if (context.packageManager != null && emailIntent.resolveActivity(context.packageManager) != null) {
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            emailIntent.putExtra(Intent.EXTRA_TEXT, message)
            context.startActivity(emailIntent)
        } else {
            showSupportDialog(context, noMailAppTitle, noMailAppBody)
        }
    }

    private fun showSupportDialog(context: Context, title: String, message: String) {
        val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
        var alertDialog: AlertDialog? = null
        builder.setTitle(title).setMessage(message)
            .setPositiveButton(context.resources.getString(R.string.dialog_ok)) { _, _ -> alertDialog?.dismiss() }
        alertDialog = builder.create()
        alertDialog.show()
    }
}
