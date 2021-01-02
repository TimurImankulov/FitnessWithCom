package com.example.fitness.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.fitness.R
import com.example.fitness.data.model.MainTraining

class HistoryAdapter(private val listener: ItemClicks) : ListAdapter<MainTraining, HistoryVH>(DiffUtilCallBack()) { //адаптер работает с DiffUtilCallBack

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryVH(view)
    }

    override fun onBindViewHolder(holder: HistoryVH, position: Int) {
        holder.bind(getItem(position), listener)
    }
}

class DiffUtilCallBack : DiffUtil.ItemCallback<MainTraining>() { // этот класс работат со списками, если он находит какой-либо новый элемент в списке он его сам добавляет
    override fun areItemsTheSame(oldItem: MainTraining, newItem: MainTraining): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MainTraining, newItem: MainTraining): Boolean {
        return oldItem.id == newItem.id
                && oldItem.startAt == newItem.startAt
                && oldItem.finishAt == newItem.finishAt
                && oldItem.distance == newItem.distance
    }
}