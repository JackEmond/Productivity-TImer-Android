package com.example.productivitytimer.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [TimerRecord::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration (from = 1, to = 2)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weightRecordDao(): TimerRecordDao
}

