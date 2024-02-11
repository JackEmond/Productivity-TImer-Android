package com.example.productivitytimer.data

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ProductivityTimerDBRepository @Inject constructor(
    //private val localDataSource: ProductivityTimerDao,
){
    fun insertTime(time: Int) {
        Log.w("Jack test", "time: $time")

        /*
        val timer = ProductivityTimerRecord(
            time = time
        )
        localDataSource.insertTimer(timer)
         */
    }


}
