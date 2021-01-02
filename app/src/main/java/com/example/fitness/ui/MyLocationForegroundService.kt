package com.example.fitness.ui

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.example.fitness.data.NotificationHelper
import com.example.fitness.data.events.TrainingEndedEvent
import com.example.fitness.data.events.UserLocationEvent
import com.google.android.gms.location.*
import com.mapbox.geojson.Point
import com.mapbox.turf.TurfMeasurement
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus

class MyLocationForegroundService: Service() { // класс для работы с запущенным сервисом

    private val job = Job()
    private val scope = CoroutineScope(job)
    private var totalLineDistance: Double = 0.0

    var fusedLocation: FusedLocationProviderClient? = null // делаем глобальным
    private val list = arrayListOf<Point>() // лист для сохранения координат перемещения из fusedLocation

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int { // метод запускает сервис и ловим интент, приходяший от startService() с MainActivity
        if (intent?.action == STOP_SERVICE_ACTION){ // если с интентом приходит STOP_SERVICE_ACTION значит это интент с кнопки stop на уведомлении
            stopForeground(true)  // останавливаем сервис и удаляем уведомление
            stopSelf()                             // полностью останаливаем сервис
        } else {
            fusedLocation = LocationServices.getFusedLocationProviderClient(applicationContext)          // Отслеживание перемещений через fusedLocation
            val  locationRequest = LocationRequest()                                                     // указываем параметры координаты приходят не реже и не чаще 10с и 3с
                    locationRequest.interval = 10_000
                    locationRequest.fastestInterval = 3_000
                    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY                    // прироритет в получении данных
            fusedLocation?.requestLocationUpdates(locationRequest, fLocListener, Looper.getMainLooper())

            startForeground(1,NotificationHelper.createNotification(applicationContext)) // запуск ForegroundService с уведомлением
            test()
        }
        return START_REDELIVER_INTENT  // флаг если сервис остановиться андроидом перезапустить его и востановить интент
    }

    private val fLocListener = object : LocationCallback(){ // слушание геолакации, сюда приходят координаты передвижения
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult?.lastLocation?.let {
                list.add(Point.fromLngLat(it.longitude,it.latitude))       // сохраняем координаты в список
                calculateDistance()
                EventBus.getDefault().post(UserLocationEvent(list, totalLineDistance))        // отправляем событие (data class) с типом UserLocationEvent
            }                                                                                 // и передаем значение пройденного расстояния
        }
    }

    private fun calculateDistance() {
        val distanceBetweenLastAndSecondToLastClickPoint: Double
        if (list.size >= 2) {
            distanceBetweenLastAndSecondToLastClickPoint = TurfMeasurement.distance(
                list[list.size - 2], list[list.size - 1]
            )

            if (distanceBetweenLastAndSecondToLastClickPoint > 0) {
                totalLineDistance += distanceBetweenLastAndSecondToLastClickPoint
            }
        }
    }

    private fun test() { // для тестирование сервиса, цикл обворачиваем в корутины чтобы не блокировать основной поток
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
        fusedLocation?.removeLocationUpdates(fLocListener) // отписываемся от получения локации
        scope.cancel()                                     // останавливаем корутины
        EventBus.getDefault().post(TrainingEndedEvent(true)) // отправляем событие (data class) с типом TrainingEndedEvent о том что тренировка закончилась
    }

    companion object{
       const val STOP_SERVICE_ACTION = "STOP_SERVICE_ACTION"
    }
}