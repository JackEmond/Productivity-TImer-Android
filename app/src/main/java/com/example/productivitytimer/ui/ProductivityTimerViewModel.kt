package com.example.productivitytimer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.productivitytimer.data.ProductivityTimerDBRepository
import com.example.productivitytimer.data.RunningTimerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val repository: ProductivityTimerDBRepository,
    runningTimerRepository: RunningTimerRepository
): ViewModel() {

    private val _time = MutableStateFlow(0) // Time in seconds
    val time: StateFlow<Int> = _time

    private val _timerPaused = MutableLiveData(false)
    val timerPaused: LiveData<Boolean> = _timerPaused

    private val timer = ProductivityTimer(scope = viewModelScope, _time = _time, _timerPaused = _timerPaused, repository = runningTimerRepository)


    fun startTimer() {

        timer.start()
    }

    fun pauseOrResumeTimer() {
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
