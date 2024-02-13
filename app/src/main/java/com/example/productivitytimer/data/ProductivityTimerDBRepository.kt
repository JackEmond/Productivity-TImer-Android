package com.example.productivitytimer.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.productivitytimer.data.local.TimerRecord
import com.example.productivitytimer.data.local.TimerRecordDao
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ProductivityTimerDBRepository @Inject constructor(
    private val localDataSource: TimerRecordDao,
){
    suspend fun insertTime(time: Int) {
        Log.w("Jack test", "time: $time")

        val timer = TimerRecord(time = time)
        localDataSource.insertTimer(timer)
    }

    fun getAllTimers(): LiveData<List<TimerRecord>> = localDataSource.getAllTimers()

    suspend fun deleteRecord(id: Int) {
        localDataSource.deleteById(id)
    }


}
