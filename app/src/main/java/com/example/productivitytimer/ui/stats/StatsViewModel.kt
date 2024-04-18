package com.example.productivitytimer.ui.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.productivitytimer.data.ProductivityTimerDBRepository
import com.example.productivitytimer.data.local.TimerRecordDao
import com.patrykandpatrick.vico.core.marker.Marker
import com.patrykandpatrick.vico.core.marker.MarkerVisibilityChangeListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject


data class TimerRecord(
    val id: Int = 0,
    var date: String = "",
    var timeInSeconds: Int = 0
) {
    val hours: Int
        get() = timeInSeconds / 3600

    val minutes: Int
        get() = (timeInSeconds % 3600) / 60

    val seconds: Int
        get() = timeInSeconds % 60
}

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val repository: ProductivityTimerDBRepository,
): ViewModel() {

    private val _graphData = MutableLiveData(mapOf("1" to 0))
    val graphData: LiveData<Map<String, Int>> = _graphData


    private val _timeProductiveThatDay = MutableLiveData( TimerRecord(
        id = 0,
        date = "",
        timeInSeconds = 0,
    ))
    val timeProductiveThatDay: MutableLiveData<TimerRecord> = _timeProductiveThatDay

    init {
        viewModelScope.launch {
            repository.getTimeProductiveEachDay(7).collect { daySums ->
               _graphData.value = transformTimersToGraphData(daySums)
                _timeProductiveThatDay.value?.timeInSeconds = _graphData.value?.entries?.lastOrNull()?.value ?: 0
            }
        }
    }

    private val earlyDate = -6


    private fun transformTimersToGraphData(timeRanEachDay: List<TimerRecordDao.TimeRanEachDay>): Map<String, Int> {
        val map = mutableMapOf<String,Int>() //Create a empty map which will contain total time productive that day and a string representing the date
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val dateFormat = SimpleDateFormat("MM/dd", Locale.US)

        val calendar = Calendar.getInstance() //Get the Current Day
        calendar.add(Calendar.DATE, -6) //Get The previous 7 days. (0 is the current day, -1 is the day prior)

        // Get the last 7 days
        for (i in 1..7) {
            map[dateFormat.format(calendar.time)] = 0 //
            calendar.add(Calendar.DATE, 1)
        }

        for(item in timeRanEachDay){
            val date = inputFormat.parse(item.day) ?: continue //Convert to a Date
            val updatedDate = dateFormat.format(date) // Format as a string
            map[updatedDate] = item.sumTime //Insert map with correct info
        }
        return map
    }

    private fun getDateSelected(index: Int): String{
        val daySelected = earlyDate + index
        val date = Calendar.getInstance()
        date.add(Calendar.DAY_OF_YEAR, daySelected)
        return  "${date.get(Calendar.MONTH) + 1}  / ${date.get(Calendar.DAY_OF_MONTH)}"
    }


    val markerVisibilityChangeListener = object : MarkerVisibilityChangeListener {
        override fun onMarkerShown(marker: Marker, markerEntryModels: List<Marker.EntryModel>) {
            markerEntryModels.forEach { entryModel ->
                timeProductiveThatDay.value = TimerRecord(
                    id = entryModel.index,
                    date = getDateSelected(entryModel.index),
                    timeInSeconds = _graphData.value?.entries?.elementAtOrNull(entryModel.index)?.value ?: 0
                )
            }
        }
        override fun onMarkerHidden(marker: Marker) {}
        override fun onMarkerMoved(marker: Marker, markerEntryModels: List<Marker.EntryModel>) {}
    }

    fun getAllTimers(): LiveData<List<TimerRecord>> {
        return repository.getAllTimers().map { list ->
            list.map { timerRecord ->
                TimerRecord(
                    id = timerRecord.id,
                    date = formatDate(timerRecord.date),
                    timeInSeconds =  timerRecord.time
                )
            }
        }
    }

    private fun formatDate(date: Long): String {
        val formatter = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
        return formatter.format(Date(date)).toString()
    }

    fun deleteTimer(id: Int) = viewModelScope.launch{
        repository.deleteRecord(id)
    }
}