package com.example.productivitytimer.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerViewModel(application: Application): AndroidViewModel(application){
    private val scope = viewModelScope
    private var job: Job? = null
    var time by mutableIntStateOf(0) // Time in seconds
    private var isActive = false


    fun startTimer() {
        startIncrementingTime()
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


    fun pauseTimer(){
        job?.cancel()
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