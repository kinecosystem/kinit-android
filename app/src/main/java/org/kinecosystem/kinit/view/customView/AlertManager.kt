package org.kinecosystem.kinit.view.customView

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.possitive_dialog.view.*
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.util.ImageUtils

class AlertManager {

    companion object {
        enum class Type {
            System,
            Positive
        }

        fun createAlert(context: Context, @StringRes title: Int, @StringRes message: Int, @StringRes positiveMessage: Int, positiveAction: (() -> Unit)?, @StringRes negativeMessage: Int? = null, negativeAction: (() -> Unit)? = null, dismiss: Boolean = true, cancelable: Boolean = true, type: Type = Type.System): AlertDialog {
            val builder = if (type == Type.System) {
                AlertDialog.Builder(context, R.style.CustomAlertDialog)
            } else {
                AlertDialog.Builder(context)
            }

            builder.setTitle(title).setMessage(
                    message).setPositiveButton(positiveMessage) { dialogInterface, _ ->
                if (dismiss) {
                    dialogInterface?.dismiss()
                }
                positiveAction?.invoke()
            }
            negativeMessage?.let {
                builder.setNegativeButton(it) { dialogInterface, _ ->
                    if (dismiss) {
                        dialogInterface?.dismiss()
                    }
                    negativeAction?.invoke()
                }
            }

            if (type == Type.Positive) {
                val dialogView = LayoutInflater.from(context).inflate(R.layout.possitive_dialog, null)
                builder.setView(dialogView)
            }

            val alertDialog = builder.create()
            if (!cancelable) {
                alertDialog.setCancelable(false)
                alertDialog.setCanceledOnTouchOutside(false)
            }
            return alertDialog
        }

        fun createAlert(context: Context, @StringRes title: Int, message: String, @StringRes positiveMessage: Int, positiveAction: (() -> Unit)?, @StringRes negativeMessage: Int? = null, negativeAction: (() -> Unit)? = null, dismiss: Boolean = true, cancelable: Boolean = true, type: Type = Type.System): AlertDialog {
            val builder = if (type == Type.System) {
                AlertDialog.Builder(context, R.style.CustomAlertDialog)
            } else {
                AlertDialog.Builder(context)
            }

            builder.setTitle(title).setMessage(
                    message).setPositiveButton(positiveMessage) { dialogInterface, _ ->
                if (dismiss) {
                    dialogInterface?.dismiss()
                }
                positiveAction?.invoke()
            }
            negativeMessage?.let {
                builder.setNegativeButton(it) { dialogInterface, _ ->
                    if (dismiss) {
                        dialogInterface?.dismiss()
                    }
                    negativeAction?.invoke()
                }
            }

            val alertDialog = builder.create()
            if (!cancelable) {
                alertDialog.setCancelable(false)
                alertDialog.setCanceledOnTouchOutside(false)
            }
            return alertDialog
        }


        fun showAlert(context: Context, @StringRes title: Int, @StringRes message: Int, @StringRes positiveMessage: Int, positiveAction: (() -> Unit)?, @StringRes negativeMessage: Int? = null, negativeAction: (() -> Unit)? = null, dismiss: Boolean = true, cancelable: Boolean = true, type: Type = Type.System) {
            createAlert(context, title, message, positiveMessage, positiveAction, negativeMessage, negativeAction, dismiss, cancelable, type).show()
        }

        fun showAlertNoIternetDismiss(context: Context) {
            AlertManager.showAlert(context, R.string.no_internet_connection, R.string.no_internet_message, R.string.dialog_ok, {})
        }

        fun showAlert(context: Context, @StringRes title: Int, message: String, @StringRes positiveMessage: Int, positiveAction: (() -> Unit)?, @StringRes negativeMessage: Int? = null, negativeAction: (() -> Unit)? = null, dismiss: Boolean = true, cancelable: Boolean = true, type: Type = Type.System) {
            createAlert(context, title, message, positiveMessage, positiveAction, negativeMessage, negativeAction, dismiss, cancelable, type).show()
        }

        fun showGeneralAlert(context: Context, @StringRes title: Int, @StringRes message: Int, @StringRes positiveMessage: Int, positiveAction: (() -> Unit),
                             @StringRes negativeMessage: Int? = null, negativeAction: (() -> Unit)? = null, imageUrl: String? = null) {
            val negativeMsg = if (negativeMessage != null) context.resources.getString(negativeMessage) else null
            showGeneralAlert(context, context.resources.getString(title), context.resources.getString(message), context.resources.getString(positiveMessage), positiveAction, negativeMsg, negativeAction, imageUrl)
        }

        fun showGeneralAlert(context: Context, title: String, message: String,
                             positiveMessage: String, positiveAction: (() -> Unit),
                             negativeMessage: String? = null, negativeAction: (() -> Unit)? = null,
                             imageUrl: String? = null, @DrawableRes imageRes: Int = R.drawable.backup_illus_popup, cancelable: Boolean? = true) {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.possitive_dialog, null)
            val alertDialog = AlertDialog.Builder(context).setCancelable(cancelable
                    ?: true).setView(dialogView).create()
            dialogView.title.text = title
            dialogView.message.text = message
            with(dialogView.positiveBtn) {
                this.text = positiveMessage
                setOnClickListener {
                    positiveAction.invoke()
                    if (cancelable != false)
                        alertDialog.dismiss()
                }
            }

            with(dialogView.negativeBtn) {
                negativeMessage?.let {
                    this.text = it
                    this.setOnClickListener {
                        negativeAction?.invoke()
                        alertDialog.dismiss()
                    }
                } ?: run {
                    visibility = View.GONE
                }
            }

            if (TextUtils.isEmpty(imageUrl)) {
                dialogView.icon.setImageResource(imageRes)
            } else {
                ImageUtils.loadImageIntoView(context, imageUrl, dialogView.icon)
            }
            alertDialog.show()
        }
    }
}