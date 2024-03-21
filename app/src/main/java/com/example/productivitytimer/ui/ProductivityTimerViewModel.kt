package com.example.productivitytimer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productivitytimer.data.ProductivityTimerDBRepository
import com.example.productivitytimer.data.RunningTimerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TimerRecord(
    val id: Int,
    val time: Int,
    val date:Long
)

@HiltViewModel
class ProductivityTimerViewModel @Inject constructor(
    private val repository: ProductivityTimerDBRepository,
    runningTimerRepository: RunningTimerRepository
): ViewModel() {
    private val _time = MutableStateFlow(-1) // Time in seconds
    val time: StateFlow<Int> = _time

    private val _timerPaused = MutableLiveData(false)
    val timerPaused: LiveData<Boolean> = _timerPaused

    private val timer = ProductivityTimer(scope = viewModelScope, _time = _time, _timerPaused = _timerPaused, repository = runningTimerRepository)


    init {
        timer.setTime()
    }

    val formattedTime: StateFlow<String> = _time.map { timeInSeconds ->
        formatTime(timeInSeconds)
    }.stateIn(viewModelScope, SharingStarted.Lazily, formatTime(_time.value))

    private fun formatTime(timeInSeconds: Int): String {
        val hours = timeInSeconds / 3600
        val minutes = (timeInSeconds % 3600) / 60
        val seconds = timeInSeconds % 60
        return String.format("%02dHRS %02dMIN %02dSEC", hours, minutes, seconds)
    }


    fun startTimer(){
        timer.start()
    }

    fun pauseOrResumeTimer() {
        timer.pauseOrResume()
    }

    fun saveTimer(){
        val timeRan = time.value
        timer.resetTimer()
        insertIntoDB(timeRan)

    }

    private fun insertIntoDB(timeRan: Int) {
        viewModelScope.launch {
            repository.insertTime(timeRan)
        }
    }

    fun cancelTimer(){
        timer.resetTimer()
    }


    fun startTimerInitial() {
        timer.initialStart()
    }



}
