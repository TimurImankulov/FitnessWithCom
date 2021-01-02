package com.example.fitness.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fitness.data.model.LatLngPoints
import com.example.fitness.data.model.MainTraining
import com.mapbox.mapboxsdk.geometry.LatLng

@TypeConverters(value = [TrainingTypeConverter::class]) // указываем TypeConverter
@Database(entities = [MainTraining::class, LatLngPoints::class], version = 1)  // класс для работы с БД
abstract class TrainingDataBase:RoomDatabase() {
    abstract fun getTrainingDao():TrainingDao
}