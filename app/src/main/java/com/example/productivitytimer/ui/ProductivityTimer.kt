package com.example.productivitytimer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductivityTimer(private val scope: CoroutineScope) {
    private var job: Job? = null

    private val _time = MutableStateFlow(0) // Time in seconds
    val time: StateFlow<Int> = _time

    private val _timerPaused = MutableLiveData(false)
    val timerPaused: LiveData<Boolean> = _timerPaused


    fun start() {
        if(_time.value == 0) startIncrementingTime()
    }

    private fun startIncrementingTime() {
        job = scope.launch {
            while (true) {
                _time.value += 1
                delay(1000L) // Wait for a second
            }
        }
    }

    fun pauseOrResume() {
        if(_timerPaused.value == true)
            resumeTimer()
        else
            pauseTimer()
    }


    private fun pauseTimer(){
        _timerPaused.value = true
        job?.cancel()
    }

    private fun resumeTimer() {
        _timerPaused.value = false
        startIncrementingTime()
    }

    fun resetTimer(){
        _time.value = 0
        job?.cancel()
        _timerPaused.value = false
    }



}