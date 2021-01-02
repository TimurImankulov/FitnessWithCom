package com.example.fitness.ui.history

import com.example.fitness.data.model.MainTraining

interface ItemClicks { // interface для обработки кликов по RecyclerView, удаляет запись при нажати на itemView
    fun clickOnDelete(item: MainTraining)
}