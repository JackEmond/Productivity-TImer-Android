package com.example.productivitytimer.viewmodel

import android.app.Application
import android.content.Intent
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.productivitytimer.service.TimerService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerViewModel(private val application: Application): AndroidViewModel(application){
    private var job: Job? = null
    var time by mutableIntStateOf(0) // Time in seconds
    private val scope = viewModelScope
    private var isActive = false


    fun startTimer() {
        startIncrementingTime()
        //startTimerService()
    }

    
    private fun startIncrementingTime(){
        job = scope.launch {
            isActive = true
            while (isActive) {
                time++
                delay(1000L) // Wait for a second
            }
        }
    }



    private fun startTimerService() {
        Intent(application, TimerService::class.java).also { intent ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                application.startForegroundService(intent)
            } else {
                application.startService(intent)
            }
        }
    }
    private fun stopTimerService() {
        //Change to override the stop instead of going to the start (if possible)
        Intent(application, TimerService::class.java).also { intent ->
            intent.action = TimerService.ACTION_STOP_SERVICE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                application.startForegroundService(intent)
            } else {
                application.startService(intent)
            }
        }
    }



    fun pauseTimer(){
        job?.cancel()
        //Pause Service
        // TODO:
    }

    fun saveTimer(){
        //TODO:
    }

    fun cancelTimer() {
        //Timer.cancel();
        job?.cancel()
        //stopTimerService()
    }



}