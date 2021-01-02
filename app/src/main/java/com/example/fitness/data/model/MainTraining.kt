package com.example.fitness.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MainTraining(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val point: LatLngPoints,
    val distance: Double,
    val duration: Long,
    val startAt : Long,
    val finishAt: Long,
    val calories: Int
)