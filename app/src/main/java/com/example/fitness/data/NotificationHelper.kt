package com.example.fitness.data

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.fitness.R
import com.example.fitness.ui.MyLocationForegroundService
import com.example.fitness.ui.MyLocationForegroundService.Companion.STOP_SERVICE_ACTION
import com.example.fitness.utils.NotificationUtils

object NotificationHelper {
    private const val CHANNEL_ID = "my_channel"
    private const val CHANNEL_NAME = "CHANNEL_NAME"
    private const val CHANNEL_DESC = "CHANNEL_DESC"

    fun createNotification(context: Context):Notification?{
        createNotificationChannel(context)

        val intent = Intent(context, MyLocationForegroundService::class.java)
        intent.action = STOP_SERVICE_ACTION

        val pIntent = PendingIntent.getService(context, 0, intent,0)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_account_balance_wallet_24)
            .setContentTitle("Title")
            .setContentText("testtesttesttesttesttesttesttest")
            .addAction(R.drawable.ic_baseline_account_balance_wallet_24,"STOP", pIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        return builder.build()
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESC
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}