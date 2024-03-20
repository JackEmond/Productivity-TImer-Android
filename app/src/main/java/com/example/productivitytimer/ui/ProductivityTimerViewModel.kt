package com.example.productivitytimer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.productivitytimer.data.ProductivityTimerDBRepository
import com.example.productivitytimer.data.RunningTimerRepository
import com.example.productivitytimer.data.local.TimerRecordDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
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

    private val _graphData = MutableLiveData<Map<String, Int>>()
    val graphData: LiveData<Map<String, Int>> = _graphData

    init {
        timer.setTime()
        viewModelScope.launch {
            repository.getTimersFromLast7Days().collect { daySums ->
                _graphData.value = transformCurrWeekOfTimersToGraphData(daySums)
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

    fun startTimerInitial() {
        timer.initialStart()
    }


    private fun transformCurrWeekOfTimersToGraphData(timeRanEachDay: List<TimerRecordDao.TimeRanEachDay>): Map<String, Int> {

        val map = mutableMapOf<String,Int>()

        val currDay  = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        val daysOfTheWeek = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

        for (i in currDay until daysOfTheWeek.size) {
            map[daysOfTheWeek[i]] = 0
        }
        for (i in 0 until currDay) {
            map[daysOfTheWeek[i]] = 0
        }


        for (t in timeRanEachDay) {
            val dayOfWeek = covertDayOfWeekFromIntToString(t.dayOfWeek)
            map[dayOfWeek] = map[dayOfWeek]?.plus(t.sumTime) ?: t.sumTime
        }

    return map
    }

    private fun covertDayOfWeekFromIntToString(dayOfWeek: String): String {
        val daysOfTheWeek = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        return daysOfTheWeek[dayOfWeek.toInt()]

    }


}
