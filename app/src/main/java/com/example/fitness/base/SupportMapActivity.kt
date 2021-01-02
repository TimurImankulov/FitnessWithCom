package com.example.fitness.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fitness.BuildConfig
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style

abstract class SupportMapActivity:AppCompatActivity() {//Базовый класс для других активити в которых будет использоваться карта, все его методы реализуются в классе наследнике

    abstract fun getResId(): Int // для подвязки верстки в дочерних классах
    abstract fun getMapViewId(): Int // для подвязки mapView в дочерних классах
    abstract fun onMapLoaded(mapBoxMap: MapboxMap, style: Style)

    protected var mapView: MapView? = null // контейнер для карты
    protected var map: MapboxMap? = null //карта

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(applicationContext, BuildConfig.API_KEY_MAPS) // подключаем карту до загрузки setContentView()
        setContentView(getResId())
        mapView = findViewById(getMapViewId())
        mapView?.onCreate(savedInstanceState) // чтобы карта работала при перевороте экрана

        mapView?.getMapAsync { mapBoxMap -> // Подгружает карту в асинхронном потоке чтобы не блокировать основной поток, стиль для карты
            map = mapBoxMap                            // переменной map приравниваем mapBoxMap, карту которую мы подгружаем
            mapBoxMap.setStyle(Style.LIGHT) { style ->
                onMapLoaded(mapBoxMap,style)         // вызовится после загрузке карты, и подгрузит стиль, ресурс для линии, клик и картинку для маркера
            }                                        // вынесли в этот класс как boilerplate, SupportMapActivity предназначен только для загрузки карты, как шаблон
        }
    }
// подключаем mapView ко всем жизненным циклам чтобы карта знала о происходящих событиях
    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }
}