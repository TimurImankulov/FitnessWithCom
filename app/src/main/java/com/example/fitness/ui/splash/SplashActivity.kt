package com.example.fitness.ui.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.fitness.R
import com.example.fitness.data.local.PreferenceHelper
import com.example.fitness.ui.main.MainActivity
import com.example.fitness.ui.onboard.OnBoardActivity
import com.example.fitness.utils.launchActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({ // делаем задержку чтобы main поток не заснул
            selectActivity()
        }, 3000)

    }

    private fun selectActivity() {
         if (PreferenceHelper.getIsFirstLaunch()) {
             launchActivity<OnBoardActivity>()
             finish()
        } else {
            launchActivity<MainActivity>()
            finish()
         }
      }
}