package com.example.productivitytimer.ui.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.productivitytimer.data.ProductivityTimerDBRepository
import com.example.productivitytimer.data.local.TimerRecordDao
import com.example.productivitytimer.ui.TimerRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject




@HiltViewModel
class StatsViewModel @Inject constructor(
    private val repository: ProductivityTimerDBRepository,
): ViewModel() {

    init {
        viewModelScope.launch {
            repository.getTimersFromLast7Days().collect { daySums ->
                _graphData.value = transformCurrWeekOfTimersToGraphData(daySums)
            }
        }
    }

    private val _graphData = MutableLiveData<Map<String, Int>>()
    val graphData: LiveData<Map<String, Int>> = _graphData


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