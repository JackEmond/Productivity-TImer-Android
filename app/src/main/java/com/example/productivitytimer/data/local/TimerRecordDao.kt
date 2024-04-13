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

    @Query("SELECT SUM(time) as sumTime, strftime('%w', date / 1000, 'unixepoch') as day FROM timerrecord WHERE date > :sevenDaysAgo GROUP BY day")
    fun getTimersFromLast7Days(sevenDaysAgo: Long): Flow<List<TimeRanEachDay>>

    @Query("SELECT date(date/1000, 'unixepoch') as day, SUM(time) as sumTime FROM timerrecord WHERE date >= :earlierDate AND date <= :laterDate GROUP BY day ORDER BY day DESC")
    fun getTimeProductiveEachDay(earlierDate: Long, laterDate: Long): Flow<List<TimeRanEachDay>>

    data class TimeRanEachDay(val day: String, val sumTime: Int)

    @Query("DELETE FROM timerrecord WHERE id = :id")
    suspend fun deleteById(id: Int)
}