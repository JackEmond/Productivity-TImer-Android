package com.example.productivitytimer.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TimerRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val time: Int,
)
