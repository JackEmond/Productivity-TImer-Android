package com.example.productivitytimer.ui

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProductivityTimer(
    private val scope: CoroutineScope,
    private val _time: MutableStateFlow<Int>,
    private val _timerPaused: MutableLiveData<Boolean>
) {
    private var job: Job? = null

    fun start() {
        if (job?.isActive == null)
            startIncrementingTime()
    }

    private fun timerIsPaused(): Boolean{
        return _timerPaused.value == true
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
        if(timerIsPaused())
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