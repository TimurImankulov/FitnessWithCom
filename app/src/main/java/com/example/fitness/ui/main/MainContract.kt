package com.example.fitness.ui.main

import com.example.fitness.data.events.UserLocationEvent
import com.example.fitness.ui.LiveCycle
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point

interface MainContract {

    interface View{
        fun showRoute(featureCollection: FeatureCollection)
   //     fun showLastRoute(points: ArrayList<Point>)
        fun changeBSState(stateCollapsed: Int)
    }

    interface Presenter: LiveCycle<View>{
        fun collectData(list: UserLocationEvent)
        fun showLastRace()
        fun checkBSState(state: Int?)
        fun saveTraining()
        fun saveCurrentTime()
    }
}