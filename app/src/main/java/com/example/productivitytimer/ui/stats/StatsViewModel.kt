package com.example.productivitytimer.ui.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.productivitytimer.data.ProductivityTimerDBRepository
import com.example.productivitytimer.data.local.TimerRecordDao
import com.example.productivitytimer.ui.timer.TimerRecord
import com.patrykandpatrick.vico.core.marker.Marker
import com.patrykandpatrick.vico.core.marker.MarkerVisibilityChangeListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject




@HiltViewModel
class StatsViewModel @Inject constructor(
    private val repository: ProductivityTimerDBRepository,
): ViewModel() {
    private val _graphData = MutableLiveData(mapOf("1" to 0f))
    val graphData: LiveData<Map<String, Float>> = _graphData

    private val _timeProductiveThatDay = MutableLiveData(0f)
    val timeProductiveThatDay: MutableLiveData<Float> = _timeProductiveThatDay


    init {
        viewModelScope.launch {
            repository.getTimeProductiveEachDay(7).collect { daySums ->
               _graphData.value = transformTimersToGraphData(daySums)
                _timeProductiveThatDay.value = _graphData.value?.entries?.lastOrNull()?.value
            }
        }
    }

    private fun transformTimersToGraphData(timeRanEachDay: List<TimerRecordDao.TimeRanEachDay>): Map<String, Float> {
        val map = mutableMapOf<String,Float>()

        val calendar = Calendar.getInstance()
        val inputFormat = SimpleDateFormat("yyyy-MM-dd")
        val dateFormat = SimpleDateFormat("MM/dd")
        calendar.add(Calendar.DATE, -6)

        // Get the last 7 days
        for (i in 1..7) {
            map[dateFormat.format(calendar.time)] = 0f
            calendar.add(Calendar.DATE, 1)
        }

        for(item in timeRanEachDay){
            val date = inputFormat.parse(item.day)
            val updatedDate = dateFormat.format(date)
            map[updatedDate] = item.sumTime.toFloat()
        }
        return map
    }

    val markerVisibilityChangeListener = object : MarkerVisibilityChangeListener {
        override fun onMarkerShown(marker: Marker, markerEntryModels: List<Marker.EntryModel>) {
            markerEntryModels.forEach { entryModel ->
                val markerValue = _graphData.value?.entries?.elementAtOrNull(entryModel.index)?.value
                updateSelectedDateInfo(markerValue)
            }
        }
        override fun onMarkerHidden(marker: Marker) {}
        override fun onMarkerMoved(marker: Marker, markerEntryModels: List<Marker.EntryModel>) {}
    }

    private fun updateSelectedDateInfo(markerKey: Float?) {
        // update TimeClicked  the time clicked
        _timeProductiveThatDay.value = markerKey

        // Show the time clicked in the previous 7 days of the timer


        // Show the time clicked in the previous 30 days of the timer


        // Show timers for the given day
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