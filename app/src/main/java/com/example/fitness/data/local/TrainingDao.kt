package com.example.fitness.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fitness.data.model.MainTraining


@Dao
interface TrainingDao {                   // класс в котором будут методы для работы с БД

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTraining(data: MainTraining)

    @Query("SELECT * FROM MainTraining")
    fun getTraining(): List<MainTraining>

    @Query("SELECT * FROM MainTraining")
    fun getTrainingLiveData(): LiveData<List<MainTraining>>

    @Query("DELETE FROM MainTraining WHERE id =:id")
    fun deleteTraining(id: Int)
}