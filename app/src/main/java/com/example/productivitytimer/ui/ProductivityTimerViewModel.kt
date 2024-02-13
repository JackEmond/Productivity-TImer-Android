package com.example.productivitytimer.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.productivitytimer.data.ProductivityTimerDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

    fun saveTimer() = viewModelScope.launch{
        repository.insertTime(time)
    }

    fun cancelTimer() {
        //Timer.cancel();
        job?.cancel()
        //stopTimerService()
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






}
