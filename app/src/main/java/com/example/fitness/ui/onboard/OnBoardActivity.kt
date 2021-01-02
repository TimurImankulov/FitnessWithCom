package com.example.fitness.ui.onboard

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.fitness.R
import com.example.fitness.data.local.PreferenceHelper
import com.example.fitness.data.model.OnBoardModel
import com.example.fitness.ui.main.MainActivity
import com.example.fitness.utils.launchActivity
import kotlinx.android.synthetic.main.activity_on_board.*

class OnBoardActivity : AppCompatActivity() {

    private val list = arrayListOf<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_board)
        setupViewPager()
        setupListeners()

    }

    private fun setupListeners() {
        onBoardViewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (isLastPage(position)) {
                    tvNext.text = getString(R.string.proceed)
                } else {
                    tvNext.text = getString(R.string.skip)
                }
            }

        })

        tvNext.setOnClickListener {
            if (isLastPage(onBoardViewPager.currentItem)) {
                PreferenceHelper.setIsFirstLaunch()
                launchActivity<MainActivity>()
                finish()
            }
            else {
                onBoardViewPager.currentItem += 1
            }
        }
    }

    private fun isLastPage(position: Int) = position == list.size - 1


    private fun setupViewPager() {
        val adapter = OnBoardAdapter(supportFragmentManager)
        onBoardViewPager.adapter = adapter
        list.add(OnBoardFragment.getInstance(OnBoardModel(getString(R.string.first_title), R.drawable.ic_baseline_android_24)))
        list.add(OnBoardFragment.getInstance(OnBoardModel(getString(R.string.second_title), R.drawable.ic_baseline_android_24)))
        list.add(OnBoardFragment.getInstance(OnBoardModel(getString(R.string.third_title), R.drawable.ic_baseline_android_24)))

        adapter.update(list)
        onBoardTabLayout.setupWithViewPager(onBoardViewPager)
    }
}