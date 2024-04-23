package com.example.productivitytimer.ui.stats

data class TimerRecord(
    val id: Int = 0,
    var date: String = "",
    var timeInSeconds: Int = 0
) {
}

fun TimerRecord.getFormattedTime(): String {
    val hours = timeInSeconds / 3600
    val minutes = (timeInSeconds % 3600) / 60
    val seconds = timeInSeconds % 60
    return when {
        hours > 0 -> String.format("%02d HRS %02d MIN %02d SEC", hours, minutes, seconds)
        minutes > 0 -> String.format("%02d MIN %02d SEC", minutes, seconds)
        else -> String.format("%02d SEC", seconds)
    }
}