package com.example.productivitytimer.data

import android.util.Log
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


}
