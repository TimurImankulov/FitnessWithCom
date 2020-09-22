package com.example.fitness.ui

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.fitness.data.NotificationHelper
import kotlinx.coroutines.*

class MyLocationForegroundService: Service() {

    private val job = Job()
    private val scope = CoroutineScope(job)

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == STOP_SERVICE_ACTION){
            stopForeground(true)
        } else {
            startForeground(1,NotificationHelper.createNotification(applicationContext))
            test()
        }
        return START_REDELIVER_INTENT
    }

    private fun test() {
        scope.launch(Dispatchers.Default){
            kotlin.run {
                for (i in 0..1000) {
                    Log.d("_____test", i.toString())
                    delay(200)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    companion object{
       const val STOP_SERVICE_ACTION = "STOP_SERVICE_ACTION"
    }
}