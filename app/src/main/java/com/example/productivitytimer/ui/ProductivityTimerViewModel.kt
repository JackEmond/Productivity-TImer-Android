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
    private val timer = ProductivityTimer(scope = viewModelScope)

    val time = timer.time
    val timerPaused = timer.timerPaused

    fun startTimer() {
        timer.start()
    }

    fun pauseTimerButtonClicked() {
        timer.pauseOrResume()
    }

    fun saveTimer() = viewModelScope.launch{
        repository.insertTime(time.value)
        timer.resetTimer()
    }

    fun cancelTimer(){
        timer.resetTimer()
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
