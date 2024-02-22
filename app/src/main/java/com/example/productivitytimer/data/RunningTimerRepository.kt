package com.example.productivitytimer.data

import android.content.Context
import android.util.Log
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
    val ELAPSED_TIME = intPreferencesKey("elapsed_time")
    val TIME_RUNNING = longPreferencesKey("time_running")
    val TIMER_PAUSED = booleanPreferencesKey("timer_paused")

}
data class RunningTimerData(val elapsedTime: Int)


class RunningTimerRepository  @Inject constructor(
    private val dataStore: DataStore<Preferences>
){

    suspend fun fetchData() = mapData(
        dataStore.data.first().toPreferences()
    )

    private fun mapData(preferences: Preferences): RunningTimerData {
        val elapsedTime = preferences[RunningTimerKeys.ELAPSED_TIME] ?: 0
        return RunningTimerData(elapsedTime)
    }

    suspend fun updateElapsedTime(elapsedTime: Int) {
        dataStore.edit { preferences ->
            preferences[RunningTimerKeys.ELAPSED_TIME] = elapsedTime
        }
        Log.w("Jack", elapsedTime.toString())
    }
}