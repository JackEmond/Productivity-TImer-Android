package com.example.productivitytimer.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TimerRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val time: Int,
    @ColumnInfo(defaultValue = "0") // Specify default value here
    val date: Long
)
