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

object NotificationHelper { // класс для создания локальных уведомлений, которые будут запускать сервис
    private const val CHANNEL_ID = "my_channel"
    private const val CHANNEL_NAME = "CHANNEL_NAME"
    private const val CHANNEL_DESC = "CHANNEL_DESC"

    fun createNotification(context: Context):Notification?{// создаем локальное уведомление
        createNotificationChannel(context)

        val intent = Intent(context, MyLocationForegroundService::class.java)
        intent.action = STOP_SERVICE_ACTION  // отправляем флаг с интентом, который говорит остановить сервис
        val pIntent = PendingIntent.getService(context, 0, intent,0)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_account_balance_wallet_24)
            .setContentTitle("Service")
            .setContentText("Service started")
            .addAction(R.drawable.ic_baseline_account_balance_wallet_24,"STOP", pIntent) // добавляем кнопку stop для остановки сервиса
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        return builder.build()
    }

    private fun createNotificationChannel(context: Context) {// создаем Channel для группировки видов уведомлений
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {// проверка версии, до 8 версии создание каналов не требовалось
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