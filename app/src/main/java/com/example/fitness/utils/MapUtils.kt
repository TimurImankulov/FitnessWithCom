package com.example.fitness.utils

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.location.Location
import com.example.fitness.BuildConfig
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object MapUtils {

    fun getDirections(
        origin: Location?, destination: LatLng,
        result: ((item: DirectionsRoute?) -> Unit)? = null
    ) {
        val client = MapboxDirections.builder()
            .accessToken(BuildConfig.API_KEY_MAPS)
            .origin(Point.fromLngLat(origin?.longitude ?: 0.0, origin?.latitude ?: 0.0))
            .destination(Point.fromLngLat(destination.longitude, destination.latitude))
            .profile(DirectionsCriteria.PROFILE_WALKING)
            .build()

        client?.enqueueCall(object : Callback<DirectionsResponse> {
            override fun onResponse(
                call: Call<DirectionsResponse>,
                response: Response<DirectionsResponse>
            ) {
                val currentRoute = response.body()?.routes()?.first()
                result?.invoke(currentRoute)
            }

            override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
            }
        })
    }

    fun createLayer(
        layerName: String,
        sourceName: String,
        lineCap: String = Property.LINE_CAP_ROUND,
        LineJoin: String = Property.LINE_JOIN_ROUND,
        lineWidth: Float = 7f,
        color: String = "#009688"
    ): LineLayer {
        val layer = getLayer(layerName, sourceName)

        layer.setProperties(
            PropertyFactory.lineCap(lineCap),
            PropertyFactory.lineJoin(LineJoin),
            PropertyFactory.lineWidth(lineWidth),
            PropertyFactory.lineColor(Color.parseColor(color))
        )

        return layer
    }

    fun addImage(
        style: Style,
        name: String,
        imageDrawable: Drawable
    ) {
        style.addImageAsync(
            name,
            BitmapUtils.getBitmapFromDrawable(imageDrawable)!!,
            true
        )
    }

    fun createSymbol(latLng: LatLng, image: String): SymbolOptions? {
        return SymbolOptions()
            .withLatLng(latLng)
            .withIconImage(image)
    }

    fun getCameraPosition(latLng: LatLng, zoom: Double = 17.0) =
        CameraUpdateFactory.newCameraPosition(CameraPosition.Builder()
            .target(latLng)
            .zoom(zoom)
            .build())

    fun locationToLatLng(location: Location?) =
        LatLng(location?.latitude ?: 0.0, location?.longitude ?: 0.0)

    fun getSource(sourceName: String) = GeoJsonSource(sourceName)
    private fun getLayer(layerName: String, sourceName: String) = LineLayer(layerName, sourceName)
}