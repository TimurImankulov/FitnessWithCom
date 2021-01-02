package com.example.fitness.ui.history

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.fitness.R
import com.example.fitness.data.model.MainTraining
import kotlinx.android.synthetic.main.item_history.view.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class HistoryVH (view: View) : RecyclerView.ViewHolder(view){ // ViewHolder для вывода тренировок и обработки кликов

    fun bind(
        item: MainTraining,
        listener: ItemClicks
    ){

        val date = Date(item.startAt)
        val dateend = Date(item.finishAt)

        val sdf = SimpleDateFormat("HH:mm:ss dd:M:yyyy", Locale.getDefault()) //ковертируем начало тренировки в обычный формат

        itemView.tvDate.text =
            itemView.context.resources.getString(R.string.start_time, sdf.format(date))

        itemView.tvDateEnd.text =
            itemView.context.resources.getString(R.string.end_time, sdf.format(dateend))

        val df = DecimalFormat("0.000")                                      // округляем число до нужного формата

        itemView.tvDistance.text = itemView.context.resources.getString(
            R.string.distance,
            df.format(item.distance)
        )

        itemView.BtnDelete.setOnClickListener {
            listener.clickOnDelete(item)
        }
    }
}