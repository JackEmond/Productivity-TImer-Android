package com.example.productivitytimer.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TimerRecordDao {

    @Insert
    suspend fun insertTimer(timer: TimerRecord)

    @Query("Select * FROM timerrecord ORDER BY DATE DESC")
    fun getAllTimers(): LiveData<List<TimerRecord>>

    @Query("DELETE FROM timerrecord WHERE id = :id")
    suspend fun deleteById(id: Int)
}