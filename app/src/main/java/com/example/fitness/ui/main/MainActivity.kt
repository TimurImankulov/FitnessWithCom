package com.example.fitness.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.fitness.R
import com.example.fitness.base.BaseMapActivity
import com.example.fitness.data.events.TrainingEndedEvent
import com.example.fitness.data.events.UserLocationEvent
import com.example.fitness.ui.MyLocationForegroundService
import com.example.fitness.ui.history.HistoryActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_bottom_sheet.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : BaseMapActivity(), MainContract.View {

    override fun getResId() = R.layout.activity_main
    override fun getMapViewId() = R.id.mapView

    private var  presenter: MainPresenter? = null
    private lateinit var bottomSheetBehaviour: BottomSheetBehavior<ConstraintLayout> // делаем глобальным bottomSheetBehaviour


//    private val intentService by lazy { // intent запускает сервис и передает в него данные
//        val intent = Intent(this, MyLocationForegroundService::class.java)
//        intent.putExtra("uhuuu","svfbadfbadfb")
//        intent
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
        setupListeners()
        presenter = MainPresenter()
        presenter?.bind(this)
        presenter?.showLastRace()
    }

    private fun setupListeners() {
        btnStart.setOnClickListener{
           startForegroundService()
        }

        btnStop.setOnClickListener{
            stopForegroundService()
        }

        btnAllTrainings.setOnClickListener {
            startActivity(Intent(applicationContext, HistoryActivity::class.java))
        }

        btnBottomSheet.setOnClickListener {                     // клик на BottomSheet опускать и подымать
            presenter?.checkBSState(bottomSheetBehaviour.state)
        }
    }

    private fun setupBottomSheet() {
        bottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehaviour.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){

            override fun onStateChanged(bottomSheet: View, newState: Int) {}
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun startForegroundService(){ // метод для запуска сервиса
        val intent = Intent(this, MyLocationForegroundService::class.java)  // запускается с помощью startService() поэтому называется запущенный сервис
        startService(intent)
    }

    private fun stopForegroundService(){
        val intent = Intent(this, MyLocationForegroundService::class.java)
        stopService(intent)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getUserData(event: UserLocationEvent){ // подписываемся на события из EventBus
        presenter?.collectData(event)     // отправляем координаты из fusedLocation в Presenter
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //??
    fun endedTraining(event: TrainingEndedEvent){ // подписываемся на события из EventBus
        presenter?.saveTraining()     // зарускаем этот метод после остановки тренировки
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)   // подписываемся на события с EventBus
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)  // отписываемся от события с EventBus
    }

    override fun showRoute(featureCollection: FeatureCollection) {  //принимаем обработанные данные из метода collectData() Presenter
        runOnUiThread {
            getDirections(featureCollection)                        //передаем featureCollection для отрисовки линии при движении маркера
        }
    }

//    override fun showLastRoute(points: ArrayList<Point>) {
//        presenter?.collectData(points)
//    }

    override fun changeBSState(stateCollapsed: Int) {  // опосускает или подымает BottomSheet, присваивает новое значение
        bottomSheetBehaviour.state = stateCollapsed
    }

    override fun onDestroy() {
        presenter?.unbind()
        super.onDestroy()
    }
}




