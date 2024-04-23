package com.example.productivitytimer.ui.stats

data class TimerRecord(
    val id: Int = 0,
    var date: String = "",
    var timeInSeconds: Int = 0
) {
    private val hours: Int
        get() = timeInSeconds / 3600

    private val minutes: Int
        get() = (timeInSeconds % 3600) / 60

    private val seconds: Int
        get() = timeInSeconds % 60

    fun getFormattedTime(): String {
        return when {
            hours > 0 -> String.format("%02d HRS %02d MIN %02d SEC", hours, minutes, seconds)
            minutes > 0 -> String.format("%02d MIN %02d SEC", minutes, seconds)
            else -> String.format("%02d SEC", seconds)
        }
    }
}