package com.example.fitness.utils

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

inline fun <reified T: AppCompatActivity> AppCompatActivity.launchActivity() {
    this.startActivity(Intent(this,T::class.java))
}

