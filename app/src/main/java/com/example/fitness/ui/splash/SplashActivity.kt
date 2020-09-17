package com.example.fitness.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.fitness.R
import com.example.fitness.data.PreferenceHelper
import com.example.fitness.ui.main.MainActivity
import com.example.fitness.ui.onboard.OnBoardActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            selectActivity()
        }, 3000)
    }

    private fun selectActivity() {
        if (PreferenceHelper.getIsFirstLaunch()) {
            startActivity(Intent(this, OnBoardActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}