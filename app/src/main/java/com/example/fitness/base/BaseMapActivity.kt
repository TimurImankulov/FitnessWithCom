package com.example.fitness.base

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import com.example.fitness.R
import com.example.fitness.utils.Constants.BISHKEK
import com.example.fitness.utils.MapUtils
import com.example.fitness.utils.PermissionUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.Symbol
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

abstract class BaseMapActivity : SupportMapActivity() {


    private var symbolManager: SymbolManager? = null //для управления маркерами на карте, точка местоположения
    private var symbol: Symbol? = null               //маркер
    protected var fusedLocation: FusedLocationProviderClient? = null // создаем дополнительный fusedLocation

    override fun onMapLoaded(mapBoxMap: MapboxMap, style: Style) { // вызовится после загрузке карты, и подгрузит стиль, ресурс для линии, клик и картинку для маркера
        setupListeners(mapBoxMap) // клик по карте для установки маркера
        loadImages(style)         //загрузка картинки для маркера
        initSource(style)         //ресурс для линии
        intitLayer(style)         //стиль для линии
        mapView?.let {
            symbolManager = SymbolManager(it, mapBoxMap, style) // инициализируем symbolManager, передаем ему параметры
        }
        if (PermissionUtils.requestLocationPermission(this)) // если разрешение есть (true) показывается местоположение пользователя
            showUserLocation()                                       // если разрешения нет (false) выводиться диалоговое окно и после нажатия yes или no
    }                                                                // срабатывает метод onRequestPermissionsResult()

    private fun intitLayer(style: Style) { // Настройка стиля для линии от одной точки до другой
        val layer = MapUtils.createLayer(LINE_LAYER, LINE_SOURCE) // линия, параметры метода по DEFAULT, можно переопределить через синие подсказки
        style.addLayer(layer)
    }

    private fun initSource(style: Style) { // Ресурс для линии, где хранятся координаты для линии
        style.addSource(MapUtils.getSource(LINE_SOURCE))
    }

//    private fun getDirections(latLng: LatLng) { // Построение пути от одной точки до другой
//        val location = map?.locationComponent?.lastKnownLocation // последняя геолакация
//        MapUtils.getDirections(location, latLng) { data -> // код в скобках выволнится только после того как CallBack вернет значение из MapUtils.getDirections
//            val source = map?.style?.getSourceAs<GeoJsonSource>(LINE_SOURCE) // данные из initSource()
//            source?.let { geoJsonSource ->     // отрисовываем линию
//                data?.geometry()?.let {
//                    source.setGeoJson(LineString.fromPolyline(it, PRECISION_6))
//                }
//            }
//        }
//    }

    protected fun getDirections(featureCollection: FeatureCollection) { // Построение пути при передвижении маркера
                                                            // в параметры приходит лист из fusedLocation
            val source = map?.style?.getSourceAs<GeoJsonSource>(LINE_SOURCE) // данные из initSource()
            source?.let { geoJsonSource ->     // отрисовываем линию
                    geoJsonSource.setGeoJson(featureCollection)
                }
            }

    private fun setupListeners(mapBoxMap: MapboxMap) { //Клик по карте с получением координат, устанавливаем маркер в нужном месте
        mapBoxMap.addOnMapClickListener {
            addMarker(it)                              // устанавливаем маркер в нужном месте
//            getDirections(it)                          // отдаем координаты маркера для точки назначения destination
            return@addOnMapClickListener true
        }
    }

    private fun loadImages(style: Style) { //Загрузка картинки для маркера
        MapUtils.addImage(
            style,
            MARKER_IMAGE,
            resources.getDrawable(R.drawable.ic_baseline_location_on_24)
        )
    }

    protected fun addMarker(latLng: LatLng) { // Добавление маркера на карту
        symbol?.let { symbolManager?.delete(it) } //если маркер уже есть удаляем его, чтобы на карте был только один маркер
                                                  // при первом запуске приходит null строчка пропускается, при последующих установках маркера null не приходит и предыдущий маркер удаляется
        val symbolOptions = MapUtils.createSymbol(latLng, MARKER_IMAGE)
        symbol = symbolManager?.create(symbolOptions)
    }

    override fun onRequestPermissionsResult( // После нажатия на диалоговом окне yes/no запускается проверка наличия permissions и показывает местоположение пользователя
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionUtils.LOCATION_REQUEST_CODE) { // проверяем код для проверки местоположения или нет
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { // проверяем есть ли разрешение, true показывается местоположение пользователя
                showUserLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun showUserLocation() { // Показывает местоположение пользователя и делает анимацию zoom
        map?.style?.let {
            fusedLocation = LocationServices.getFusedLocationProviderClient(this)               // подключаем fusedLocation
            val locationComponentOptions = LocationComponentOptions.builder(this)
                .build()

            val locationComponentActivationOptions = LocationComponentActivationOptions
                .builder(this,it)
                .locationComponentOptions(locationComponentOptions)
                .build()

            val locationComponent = map?.locationComponent
            locationComponent?.activateLocationComponent(
                LocationComponentActivationOptions.builder(applicationContext, it).build()
            )

            locationComponent?.activateLocationComponent(locationComponentActivationOptions)

            locationComponent?.isLocationComponentEnabled = true
            locationComponent?.cameraMode = CameraMode.TRACKING
            locationComponent?.renderMode = RenderMode.NORMAL

            fusedLocation?.lastLocation?.addOnSuccessListener {// сюда приходят координаты местоположения из fusedLocation
                if (it == null){
                    animateCamera(BISHKEK)                     // если координат нет используем координаты по умолчанию
                } else{
                    animateCamera(LatLng(it.latitude,it.longitude))
                }
            }
        }
    }


    private fun animateCamera(latLng: LatLng, zoom: Double = CAMERA_ZOOM) { // делает анимацию наводит на местоположение
        Handler(Looper.getMainLooper()).postDelayed({
            map?.animateCamera(
                MapUtils.getCameraPosition(latLng, zoom),
                CAMERA_DURATION
            )
        }, 1000)
    }

    companion object {
        private const val MARKER_IMAGE = "MARKER_IMAGE"
        private const val LINE_SOURCE = "LINE_SOURCE"
        private const val LINE_LAYER = "LINE_LAYER"
        private const val CAMERA_DURATION = 6000
        private const val CAMERA_ZOOM = 17.0
    }
}
