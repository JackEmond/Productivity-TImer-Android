package com.example.productivitytimer.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productivitytimer.data.ProductivityTimerDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

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








}
