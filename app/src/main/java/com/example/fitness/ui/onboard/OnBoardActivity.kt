package com.example.fitness.ui.onboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fitness.R
import com.example.fitness.data.PreferenceHelper
import com.example.fitness.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_on_board.*

class OnBoardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_board)
        setupListeners()
    }

    private fun setupListeners() {
        btnNext.setOnClickListener {
            PreferenceHelper.setIsFirstLaunch()
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}