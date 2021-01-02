package com.example.fitness.data.local

import android.text.TextUtils
import androidx.room.TypeConverter
import com.example.fitness.data.model.LatLngPoints
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mapbox.geojson.Point

object TrainingTypeConverter {

    @JvmStatic
    @TypeConverter
    fun coordToString(model: LatLngPoints):String{
        return Gson().toJson(model)
    }

    @JvmStatic
    @TypeConverter
    fun coordToString(text:String):LatLngPoints?{
        if (TextUtils.isEmpty(text))
            return null
        return Gson().fromJson(text, LatLngPoints::class.java)
    }

    @JvmStatic
    @TypeConverter
    fun pointsToString(model: List<Point>):String{
        return Gson().toJson(model)
    }

    @JvmStatic
    @TypeConverter
    fun pointsToObject(text:String?):List<Point>? {
        if (text == null) return mutableListOf()
        val typeToken = object : TypeToken<List<Point>>() {}.type
        return Gson().fromJson(text, typeToken)
    }
}