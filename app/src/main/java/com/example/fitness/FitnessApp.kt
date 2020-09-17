package com.example.fitness

import android.app.Application
import com.example.fitness.data.PreferenceHelper

class FitnessApp : Application() {

    override fun onCreate() {
        super.onCreate()
        PreferenceHelper.init(applicationContext)
    }
}