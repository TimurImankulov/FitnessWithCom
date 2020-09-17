package com.example.fitness.ui.main

import android.os.Bundle
import com.example.fitness.R
import com.example.fitness.base.BaseMapActivity
import com.example.fitness.utils.showLongToast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseMapActivity() {
    override fun getResId() = R.layout.activity_main
    override fun getMapViewId() = R.id.mapView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        fab.setOnClickListener{
            showLongToast(R.string.app_name)
        }
    }
}



