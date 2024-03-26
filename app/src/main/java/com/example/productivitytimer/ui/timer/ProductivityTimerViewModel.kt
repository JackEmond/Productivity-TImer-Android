package com.example.productivitytimer.ui.timer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productivitytimer.data.ProductivityTimerDBRepository
import com.example.productivitytimer.data.RunningTimerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
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
    private val runningTimerRepository: RunningTimerRepository
): ViewModel() {

    private val _time = MutableStateFlow(-1) // Time in seconds
    val time: StateFlow<Int> = _time

    private val _timerPaused = MutableLiveData(false)
    val timerPaused: LiveData<Boolean> = _timerPaused

    init {
        setTime()
    }

    private fun setTime(){
        viewModelScope.launch {
            val liveData = runningTimerRepository.getTimerData()
            _time.value = liveData.time
            _timerPaused.value = liveData.isPaused
        }
    }

    fun startTimer(){
        viewModelScope.launch {
            val liveData = runningTimerRepository.getTimerData()
            _time.value = liveData.time
            _timerPaused.value = liveData.isPaused

            if (!timerIsPaused() && job?.isActive != true) {
                startIncrementingTime()
            }
        }
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

    fun pauseOrResumeTimer() {
        if(timerIsPaused())
            resumeTimer()
        else
            pauseTimer()
    }

    fun saveTimer(){
        val timeRan = _time.value
        resetTimer()
        insertIntoDB(timeRan)

    }

    private fun insertIntoDB(timeRan: Int) {
        viewModelScope.launch {
            repository.insertTime(timeRan)
        }
    }

    fun cancelTimer(){
        resetTimer()
    }

    private var job: Job? = null

    private fun timerIsPaused(): Boolean{
        return _timerPaused.value == true
    }
    private fun timerFlow(startFrom: Int): Flow<Int> = flow {
        var time = startFrom
        while (true) {
            emit(time)
            time += 1
            delay(1000L)
        }
    }

    private fun startIncrementingTime() {
        job?.cancel()
        job = viewModelScope.launch {
            timerFlow(_time.value).collect { currentTime ->
                _time.value = currentTime
            }
        }
    }

    private fun pauseTimer(){
        _timerPaused.value = true
        job?.cancel()
        viewModelScope.launch {
            runningTimerRepository.timerPaused(_time.value)
        }
    }

    private fun resumeTimer() {
        _timerPaused.value = false
        startIncrementingTime()

        viewModelScope.launch {
            runningTimerRepository.resumeTimer()
        }
    }

    private fun resetTimer(){
        job?.cancel()
        _time.value = 0
        _timerPaused.value = true
        viewModelScope.launch {
            runningTimerRepository.resetTimer()
        }
    }


}
