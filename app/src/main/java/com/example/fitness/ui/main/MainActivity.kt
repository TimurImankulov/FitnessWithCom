package com.example.fitness.ui.main

import android.content.Intent
import android.os.Bundle
import com.example.fitness.R
import com.example.fitness.base.BaseMapActivity
import com.example.fitness.ui.MyLocationForegroundService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseMapActivity() {
    override fun getResId() = R.layout.activity_main
    override fun getMapViewId() = R.id.mapView

    private val intentService by lazy {
        val intent = Intent(this, MyLocationForegroundService::class.java)
        intent.putExtra("uhuuu","svfbadfbadfb")
        intent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        fab.setOnClickListener{
            startForegroundService()
        }
    }

    private fun startForegroundService(){
        startService(intentService)
    }
}



