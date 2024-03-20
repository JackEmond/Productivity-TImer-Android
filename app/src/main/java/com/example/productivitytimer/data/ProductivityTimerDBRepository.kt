package com.example.productivitytimer.data

import androidx.lifecycle.LiveData
import com.example.productivitytimer.data.local.TimerRecord
import com.example.productivitytimer.data.local.TimerRecordDao
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ProductivityTimerDBRepository @Inject constructor(
    private val localDataSource: TimerRecordDao,
){
    suspend fun insertTime(time: Int) {
        val timer = TimerRecord(
            time = time,
            date= System.currentTimeMillis()
        )
        localDataSource.insertTimer(timer)
    }

    fun getAllTimers(): LiveData<List<TimerRecord>> = localDataSource.getAllTimers()

    suspend fun deleteRecord(id: Int) {
        localDataSource.deleteById(id)
    }

    fun getTimersFromLast7Days(): LiveData<List<TimerRecord>>{
        //Get time from 7 days ago at midnight
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)


        val currentTime = calendar.timeInMillis
        return localDataSource.getTimersFromLast7Days(currentTime)
    }



}
