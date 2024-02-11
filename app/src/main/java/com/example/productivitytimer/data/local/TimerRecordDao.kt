package com.example.productivitytimer.data.local

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface TimerRecordDao {

    @Insert
    suspend fun insertTimer(timer: TimerRecord)
}