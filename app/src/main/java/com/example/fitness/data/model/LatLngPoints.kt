package com.example.fitness.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mapbox.geojson.Point

@Entity
data class LatLngPoints(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val points: ArrayList<Point>
)