package com.example.productivitytimer.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerRecordDao {

    @Insert
    suspend fun insertTimer(timer: TimerRecord)

    @Query("Select * FROM timerrecord ORDER BY DATE DESC")
    fun getAllTimers(): LiveData<List<TimerRecord>>

    @Query("SELECT SUM(time) as sumTime, strftime('%w', date / 1000, 'unixepoch') as dayOfWeek FROM timerrecord WHERE date > :sevenDaysAgo GROUP BY dayOfWeek")
    fun getTimersFromLast7Days(sevenDaysAgo: Long): Flow<List<TimeRanEachDay>>

    data class TimeRanEachDay(val dayOfWeek: String, val sumTime: Int)

    @Query("DELETE FROM timerrecord WHERE id = :id")
    suspend fun deleteById(id: Int)
}