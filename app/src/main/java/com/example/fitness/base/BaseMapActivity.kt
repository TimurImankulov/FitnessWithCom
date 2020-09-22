package com.example.fitness.base

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import com.example.fitness.R
import com.example.fitness.utils.MapUtils
import com.example.fitness.utils.PermissionUtils
import com.mapbox.core.constants.Constants.PRECISION_6
import com.mapbox.geojson.LineString
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.Symbol
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

abstract class BaseMapActivity : SupportMapActivity() {


    private var symbolManager: SymbolManager? = null
    private var symbol: Symbol? = null

    override fun onMapLoaded(mapBoxMap: MapboxMap, style: Style) {
        setupListeners(mapBoxMap)
        loadImages(style)
        initSource(style)
        intitLayer(style)
        mapView?.let {
            symbolManager = SymbolManager(it, mapBoxMap, style)
        } //чем управляет SymbolManager?
        if (PermissionUtils.requestLocationPermission(this))
            showUserLocation()
    }

    private fun intitLayer(style: Style) { // Настройка параметров линии
        val layer = MapUtils.createLayer(LINE_LAYER, LINE_SOURCE)
        style.addLayer(layer)
    }

    private fun initSource(style: Style) { // Ресурс для линии
        style.addSource(MapUtils.getSource(LINE_SOURCE))
    }

    private fun getDirections(latLng: LatLng) { // получение местоположения и рассчет маршрута между двумя точками
        val location = map?.locationComponent?.lastKnownLocation
        MapUtils.getDirections(location, latLng) { data ->
            val source = map?.style?.getSourceAs<GeoJsonSource>(LINE_SOURCE)
            source?.let { geoJsonSource ->
                data?.geometry()?.let {
                    source.setGeoJson(LineString.fromPolyline(it, PRECISION_6))
                }
            }
        }
    }

    private fun setupListeners(mapBoxMap: MapboxMap) { //Клик по карте с получением координат
        mapBoxMap.addOnMapClickListener {
            addMarker(it)
            getDirections(it)
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

        val symbolOptions = MapUtils.createSymbol(latLng, MARKER_IMAGE)
        symbol = symbolManager?.create(symbolOptions)
    }

    override fun onRequestPermissionsResult( // Проверка наличия permissions и показывает местоположение пользователя
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionUtils.LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showUserLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun showUserLocation() { // Показывает местоположение пользователя и делает анимацию zoom
        map?.style?.let {
            val locationComponent = map?.locationComponent
            locationComponent?.activateLocationComponent(
                LocationComponentActivationOptions.builder(applicationContext, it).build()
            )

            locationComponent?.isLocationComponentEnabled = true
            locationComponent?.cameraMode = CameraMode.TRACKING
            locationComponent?.renderMode = RenderMode.NORMAL

            val location = locationComponent?.lastKnownLocation
            val latLng = MapUtils.locationToLatLng(location)

            animateCamera(latLng)
        }
    }


    private fun animateCamera(latLng: LatLng, zoom: Double = CAMERA_ZOOM) {
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
