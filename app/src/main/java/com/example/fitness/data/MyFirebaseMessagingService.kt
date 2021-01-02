package com.example.fitness.data

import com.example.fitness.utils.NotificationUtils
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService:FirebaseMessagingService() { // отлавливаем уведомления с firebase

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) { //сюда приходят уведомления с firebase, RemoteMessage - сообщения на firebase
        super.onMessageReceived(p0)
        NotificationUtils.createNotification(applicationContext, // firebase уведомления передаются в этот метод
            p0.notification?.title, // title с firebase
            p0.notification?.body // текст сообщения с firebase
        )
    }
}