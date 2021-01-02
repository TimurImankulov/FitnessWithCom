package com.example.fitness.ui.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.fitness.FitnessApp
import com.example.fitness.R
import com.example.fitness.data.model.MainTraining
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity(), ItemClicks { // класс для вывода тренировок из бд

    private val adapter by lazy {          // делаем глобальный адаптер
        HistoryAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setupRecycler()
    }

    private fun setupRecycler() {  // берем данные из бд через LiveData если что-то изменилось LiveData это отобразит
        rvHistory.adapter = adapter

        FitnessApp.app?.getDB()?.getTrainingDao()?.getTrainingLiveData()?.observe(this, Observer {
            adapter.submitList(it)
        })
    }

        override fun clickOnDelete(item: MainTraining) { // по клику на itemView удаляем тренировку из бд
            GlobalScope.launch {
                kotlin.runCatching {
                    FitnessApp.app?.getDB()?.getTrainingDao()?.deleteTraining(item.id)
                }
            }
        }
    }
