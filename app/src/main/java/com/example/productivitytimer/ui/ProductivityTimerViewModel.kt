package com.example.productivitytimer.ui

import androidx.lifecycle.LiveData
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


    fun pauseTimer(){
        cancelTimer()
    }

    fun saveTimer() = viewModelScope.launch{
        repository.insertTime(time.value)
        cancelTimer()
    }

    fun cancelTimer() {
        job?.cancel()
        _time.value = 0
    }



    fun getAllTimers(): LiveData<List<TimerRecord>> {
        return repository.getAllTimers().map { list ->
            list.map { timerRecord ->
                TimerRecord(
                    id = timerRecord.id,
                    time = timerRecord.time,
                )
            }
        }
    }

    fun deleteTimer(id: Int) = viewModelScope.launch{
        repository.deleteRecord(id)
    }


}
