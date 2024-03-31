package com.example.productivitytimer.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "running_timer")

private object RunningTimerKeys {
    val ELAPSED_TIME = intPreferencesKey("elapsed_time") //However long the timer ran before it was paused (if timer was never paused this is zero)
    val TIMER_START_TIME = longPreferencesKey("time_running") //The time the active timer has currently ran for (if the timer is paused this is zero)
    val TIMER_PAUSED = booleanPreferencesKey("timer_paused")
    val TIMER_RUNNING = booleanPreferencesKey("timer_running")

}
data class RunningTimerData(
    val time: Int,
    val isPaused: Boolean,
)


class RunningTimerRepository  @Inject constructor(
    private val dataStore: DataStore<Preferences>
){
    suspend fun getTimerData() = mapPreferences(
        dataStore.data.first().toPreferences()
    )

    private fun mapPreferences(preferences: Preferences): RunningTimerData {
        val timerRunning = preferences[RunningTimerKeys.TIMER_RUNNING] ?: true

        if (!timerRunning) return RunningTimerData(time = 0, isPaused = false)

        val timerPaused = preferences[RunningTimerKeys.TIMER_PAUSED] ?: false
        val timerStartTime = preferences[RunningTimerKeys.TIMER_START_TIME] ?: 0L
        val elapsedTime = preferences[RunningTimerKeys.ELAPSED_TIME] ?: 0

        val time =
            if(timerPaused || timerStartTime == 0L){
                elapsedTime
            }
        else{
            val currentTime = System.currentTimeMillis()
            val timeElapsedSinceTimerResumed = (longInSeconds(currentTime) - longInSeconds(timerStartTime)).toInt()
            elapsedTime +  timeElapsedSinceTimerResumed
        }

        return RunningTimerData(time = time, isPaused = timerPaused)
    }

    private fun longInSeconds(time: Long): Long {
        return (time/1000)
    }

    suspend fun timerPaused(time: Int) = dataStore.edit {
        preferences ->
            preferences[RunningTimerKeys.ELAPSED_TIME] = time
            preferences[RunningTimerKeys.TIMER_PAUSED] = true
    }

    suspend fun resetTimer() = dataStore.edit {
        preferences ->
            preferences[RunningTimerKeys.TIMER_RUNNING] = false
            preferences[RunningTimerKeys.ELAPSED_TIME] = 0
            preferences[RunningTimerKeys.TIMER_PAUSED] = false
            preferences[RunningTimerKeys.TIMER_START_TIME] = 0L
    }

    suspend fun resumeTimer() = dataStore.edit {
        preferences ->
            preferences[RunningTimerKeys.TIMER_PAUSED] = false
            preferences[RunningTimerKeys.TIMER_START_TIME] = System.currentTimeMillis()
            preferences[RunningTimerKeys.TIMER_RUNNING] = true

    }

}