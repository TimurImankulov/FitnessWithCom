package com.example.fitness.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.fitness.MainActivity2
import com.example.fitness.R
import com.example.fitness.ui.main.MainActivity

object NotificationUtils {

    private const val CHANNEL_ID = "my_channel"
    private const val CHANNEL_NAME = "CHANNEL_NAME"
    private const val CHANNEL_DESC = "CHANNEL_DESC"

    fun createNotification(
        context: Context,
        title: String?,
        body: String?
    ) {
        createNotificationChannel(
            context
        )

        val pIntent = TaskStackBuilder.create(context)
            .addNextIntent(Intent(context,MainActivity::class.java))
            .addNextIntent(Intent(context,MainActivity2::class.java))
            .getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context,
            CHANNEL_ID
        )
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pIntent)
            .setSmallIcon(R.drawable.ic_baseline_accessibility_new_24)
            .build()
        showNotification(
            builder,
            context
        )
    }

    fun showNotification(builder: Notification, context: Context) {
        NotificationManagerCompat.from(context).notify(1, builder)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME, importance)
            channel.description =
                CHANNEL_DESC

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}