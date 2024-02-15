package com.example.productivitytimer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.productivitytimer.data.ProductivityTimerDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TimerRecord(
    val id: Int,
    val time: Int,
    val date:Long
)


@HiltViewModel
class ProductivityTimerViewModel @Inject constructor(
    private val repository: ProductivityTimerDBRepository

): ViewModel() {
    private val scope = viewModelScope
    private var job: Job? = null

    private val _time = MutableStateFlow(0) // Time in seconds
    val time: StateFlow<Int> = _time


    fun startTimer() {
        if(_time.value == 0) startIncrementingTime()
    }

    private fun startIncrementingTime(){
        job = scope.launch {
            while (true) {
                _time.value += 1
                delay(1000L) // Wait for a second
            }
        }
    }

    private val _timerPaused = MutableLiveData(false)
    val timerPaused: LiveData<Boolean> = _timerPaused

    fun pauseTimerButtonClicked() {
        if(_timerPaused.value == true)
            resumeTimer()
        else
            pauseTimer()
    }

    private fun pauseTimer(){
        _timerPaused.value = true
        job?.cancel()
    }


    private fun resumeTimer(){
        _timerPaused.value = false
        startIncrementingTime()
    }

    fun saveTimer() = viewModelScope.launch{
        repository.insertTime(time.value)
        resetTimer()
    }

    fun cancelTimer() {
        resetTimer()
    }

    private fun resetTimer(){
        _time.value = 0
        job?.cancel()
        _timerPaused.value = false
    }


    fun getAllTimers(): LiveData<List<TimerRecord>> {
        return repository.getAllTimers().map { list ->
            list.map { timerRecord ->
                TimerRecord(
                    id = timerRecord.id,
                    time = timerRecord.time,
                    date = timerRecord.date
                )
            }
        }
    }

    fun deleteTimer(id: Int) = viewModelScope.launch{
        repository.deleteRecord(id)
    }



}
