package org.kinecosystem.kinit.view.customView

import android.content.Context
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
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

        fun showPositiveAlert(context: Context, title: String, message: String,
                              positiveMessage: String, positiveAction: (() -> Unit),
                              negativeMessage: String? = null, negativeAction: (() -> Unit)? = null,
                              imageUrl: String? = null) {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.possitive_dialog, null)
            val alertDialog = AlertDialog.Builder(context).setView(dialogView).create()
            dialogView.title.text = title
            dialogView.message.text = message
            with(dialogView.positiveBtn) {
                this.text = positiveMessage
                setOnClickListener {
                    positiveAction.invoke()
                    alertDialog.dismiss()
                }
            }

            with(dialogView.negativeBtn) {
                negativeMessage?.let {
                    this.text = it
                    negativeAction?.let {
                        this.setOnClickListener {
                            negativeAction.invoke()
                            alertDialog.dismiss()
                        }
                    }
                } ?: run {
                    visibility = View.GONE
                }
            }
            imageUrl?.let {
                if (!it.isEmpty()) {
                    ImageUtils.loadImageIntoView(context, it, dialogView.icon)
                }
            }
            alertDialog.show()
        }

        fun showPositiveAlert(context: Context, @StringRes title: Int, @StringRes message: Int, @StringRes positiveMessage: Int, positiveAction: (() -> Unit),
                              @StringRes negativeMessage: Int? = null, negativeAction: (() -> Unit)? = null, imageUrl: String? = null) {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.possitive_dialog, null)
            val alertDialog = AlertDialog.Builder(context).setView(dialogView).create()
            dialogView.title.text = context.resources.getString(title)
            dialogView.message.text = context.resources.getString(message)
            with(dialogView.positiveBtn) {
                this.text = context.resources.getString(positiveMessage)
                setOnClickListener {
                    positiveAction.invoke()
                    alertDialog.dismiss()
                }
            }

            with(dialogView.negativeBtn) {
                negativeMessage?.let {
                    this.text = context.resources.getString(it)
                    negativeAction?.let {
                        this.setOnClickListener {
                            negativeAction.invoke()
                            alertDialog.dismiss()
                        }
                    }
                } ?: run {
                    visibility = View.GONE
                }
            }
            imageUrl?.let {
                if (!it.isEmpty()) {
                    ImageUtils.loadImageIntoView(context, it, dialogView.icon)
                }
            }
            alertDialog.show()
        }

    }

}