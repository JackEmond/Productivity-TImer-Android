package com.example.productivitytimer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [TimerRecord::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weightRecordDao(): TimerRecordDao
}