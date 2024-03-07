package com.example.productivitytimer.ui

import androidx.lifecycle.MutableLiveData
import com.example.productivitytimer.data.RunningTimerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductivityTimer @Inject constructor(
    private val scope: CoroutineScope,
    private val _time: MutableStateFlow<Int>,
    private val _timerPaused: MutableLiveData<Boolean>,
    private val repository: RunningTimerRepository
) {
    private var job: Job? = null

    fun initialStart() {
        scope.launch {
            repository.resumeTimer()
        }
        start()
    }

    fun setTime(){
        scope.launch {
            val liveData = repository.getTimerData()
            _time.value = liveData.time
            _timerPaused.value = liveData.isPaused
        }
    }

    fun start() {
        getValues()
    }

    private fun getValues() {
        scope.launch {
            val liveData = repository.getTimerData()
            _time.value = liveData.time
            _timerPaused.value = liveData.isPaused

            if (!timerIsPaused() && job?.isActive != true) {
                startIncrementingTime()
            }
        }
    }

    private fun timerIsPaused(): Boolean{
        return _timerPaused.value == true
    }

    ///This should be changed to a flow
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

        scope.launch {
            repository.timerPaused(_time.value)
        }
    }

    private fun resumeTimer() {
        _timerPaused.value = false
        startIncrementingTime()

        scope.launch {
            repository.resumeTimer()
        }
    }

    fun resetTimer(){
        job?.cancel()
        _time.value = 0
        _timerPaused.value = true
        scope.launch {
            repository.resetTimer()
        }
    }



}