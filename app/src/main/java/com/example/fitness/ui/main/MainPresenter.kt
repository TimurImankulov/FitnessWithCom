package com.example.fitness.ui.main

import android.util.Log
import com.example.fitness.FitnessApp
import com.example.fitness.data.events.UserLocationEvent
import com.example.fitness.data.model.LatLngPoints
import com.example.fitness.data.model.MainTraining
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import kotlinx.coroutines.*

class MainPresenter : MainContract.Presenter {

    private val scope = CoroutineScope(Job()) // создаем локальный scope, который перестанет работать после унитожения активити
    private var view: MainContract.View? = null
    private var list = arrayListOf<Point>() // для промежуточного хранения листа из координат передвижения
    private var distance: Double = 0.0
    private var startTime: Long = 0

    override fun collectData(data: UserLocationEvent) { // сюда приходят данные из fusedLocation, лист из Point записывается в бд
        if (data.list.size == 1) saveCurrentTime()           // сохраняем время начало тренировки
        this.list = data.list                               // а затем конвертируется в featureCollection и передается на крату для отрисовки линии при пердвижении
        this.distance = data.distance                       // записываем значение пройденного расстояния
        val lineString = LineString.fromLngLats(data.list)
        val featureCollection = FeatureCollection.fromFeature(Feature.fromGeometry(lineString)) // конвертирует лист из Point в тип featureCollection, карта работает с этим типом
        view?.showRoute(featureCollection)
    }

    override fun showLastRace() {  // достаем данные из бд
        scope.launch(Dispatchers.IO) {
            val data = FitnessApp.app?.getDB()?.getTrainingDao()?.getTraining()
            Log.d("sdvsdv", "SVDSDV")
       //     data?.point?.points?.let { view?.showLastRoute(it) } // передаем в MainActivity
        }
    }

    override fun checkBSState(state: Int?) {
        if (state == BottomSheetBehavior.STATE_EXPANDED) {
            view?.changeBSState(BottomSheetBehavior.STATE_COLLAPSED)
        } else if (state == BottomSheetBehavior.STATE_COLLAPSED) {
            view?.changeBSState(BottomSheetBehavior.STATE_EXPANDED)
        }
    }

    override fun saveTraining() {  // этот метод вызывается после того как тренировка закончилась
        saveInDB(list)
    }

    override fun saveCurrentTime() {                    // сохранение времени начало тренировки
            startTime = System.currentTimeMillis()
    }

    private fun saveInDB(list : ArrayList<Point>){ // метод для сохранения в бд
        scope.launch(Dispatchers.IO) {
            val data = getTrainingModel(list)                       //записываем в переменную data class
            FitnessApp.app?.getDB()?.getTrainingDao()?.addTraining(data) // addTraining принимает data class
        }
    }

    private fun getTrainingModel(list: ArrayList<Point>): MainTraining {  // класс создает data class для того чтобы сохранить  в бд
        return MainTraining(
            point = LatLngPoints(points = list),
            distance = distance,
            duration = System.currentTimeMillis() - startTime,
            startAt = startTime,
            finishAt = System.currentTimeMillis(), // записываем время на момент остановки тренировки
            calories = 124
        )
    }

    override fun bind(view: MainContract.View) {
        this.view = view
    }

    override fun unbind() {
        scope.cancel()         // закрываем localscope при уничтожении MainActivity
        this.view = null
    }
}