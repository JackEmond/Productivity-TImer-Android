package com.example.productivitytimer.data

import androidx.lifecycle.LiveData
import com.example.productivitytimer.data.local.TimerRecord
import com.example.productivitytimer.data.local.TimerRecordDao
import kotlinx.coroutines.flow.Flow
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

    fun getTimeProductiveEachDay(numberOfDaysAgo: Int): Flow<List<TimerRecordDao.TimeRanEachDay>> {
        val currentDay = Calendar.getInstance()
        val laterTime = currentDay.timeInMillis

        val earlierDate = Calendar.getInstance()
        earlierDate.add(Calendar.DAY_OF_YEAR, -numberOfDaysAgo+1)
        earlierDate.set(Calendar.HOUR_OF_DAY, 0)
        earlierDate.set(Calendar.MINUTE, 0)
        earlierDate.set(Calendar.SECOND, 0)
        earlierDate.set(Calendar.MILLISECOND, 0)
        val earlierTime = earlierDate.timeInMillis

        return localDataSource.getTimeProductiveEachDay(laterDate = laterTime, earlierDate = earlierTime)
    }



}
