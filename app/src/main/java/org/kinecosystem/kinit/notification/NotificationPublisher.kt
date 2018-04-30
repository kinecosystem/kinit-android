package org.kinecosystem.kinit.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.Push
import org.kinecosystem.kinit.view.MainActivity

class NotificationPublisher(private val context: Context, private val analytics: Analytics) {

    private companion object {
        const val ENGAGEMENT_CHANNEL_ID: String = "kinit.push.${Push.TYPE_ENGAGEMENT}"
        const val ENGAGEMENT_NOTIF_ID: Int = 100
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannel(ENGAGEMENT_CHANNEL_ID, Push.TYPE_ENGAGEMENT,
                "system push notifications for user engagement")
        }
    }


    fun notify(id: String, notificationData: Push.NotificationMessage) {
        val actionIntent = Intent(context, MainActivity::class.java)
        actionIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        actionIntent.putExtra(MainActivity.REPORT_PUSH_ID_KEY, id)
        actionIntent.putExtra(MainActivity.REPORT_PUSH_TEXT_KEY, notificationData.body)
        val pendingIntent = PendingIntent.getActivity(context, 0, actionIntent, 0)

        val contentTitle = if (notificationData.title.isNullOrEmpty())
            context.getString(R.string.app_name) else notificationData.title

        val builder = NotificationCompat.Builder(context, ENGAGEMENT_CHANNEL_ID)
            .setContentTitle(contentTitle)
            .setContentText(notificationData.body)
            .setSmallIcon(R.drawable.k_notif)
            .setColor(context.resources.getColor(R.color.status_bar_color))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(ENGAGEMENT_NOTIF_ID, builder.build())
        analytics.logEvent(Events.Analytics.ViewEngagementPush(id, notificationData.body))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupChannel(id: String, name: String, description: String) {
        val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = description
        val notificationManager = context.getSystemService<NotificationManager>(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(channel)

    }
}
