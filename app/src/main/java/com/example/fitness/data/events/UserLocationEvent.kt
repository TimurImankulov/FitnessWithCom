package com.example.fitness.data.events

import com.mapbox.geojson.Point

data class UserLocationEvent(     // событие передающее лист и значение пройденного расстояния
    val list: ArrayList<Point>,
    val distance: Double
)

data class TrainingEndedEvent(     // событие окончания тренировки
    val isFinished: Boolean
)