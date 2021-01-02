package com.example.fitness

import android.app.Application
import androidx.room.Room
import com.example.fitness.data.local.PreferenceHelper
import com.example.fitness.data.local.TrainingDataBase

class FitnessApp : Application() {

    private var db : TrainingDataBase? = null

    override fun onCreate() {
        super.onCreate()
        PreferenceHelper.init(applicationContext)
        app = this

        db = Room.databaseBuilder(applicationContext, TrainingDataBase::class.java, DB_NAME)
            .build()
    }

    fun getDB() = db

    companion object{
        var app: FitnessApp? = null
        private const val DB_NAME = "MY_DB"
    }
}