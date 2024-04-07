package com.example.productivitytimer.ui.stats

import android.util.Log
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
    private val _graphData = MutableLiveData(mapOf("1" to 0f))
    val graphData: LiveData<Map<String, Float>> = _graphData


    private fun transformCurrWeekOfTimersToGraphData(timeRanEachDay: List<TimerRecordDao.TimeRanEachDay>): Map<String, Float> {

        val map = mutableMapOf<String,Float>()

        val currDay  = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        val daysOfTheWeek = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

        for (i in currDay until daysOfTheWeek.size) {
            map[daysOfTheWeek[i]] = 0F
        }
        for (i in 0 until currDay) {
            map[daysOfTheWeek[i]] = 0F
        }

        var maxTime = 0
        for (t in timeRanEachDay) {
            if (t.sumTime > maxTime) maxTime = t.sumTime
        }


        for (t in timeRanEachDay) {
            val dayOfWeek = covertDayOfWeekFromIntToString(t.dayOfWeek)
            val time = convertTimeToCorrectFormat(t.sumTime, maxTime = maxTime)
            map[dayOfWeek] = map[dayOfWeek]?.plus(time) ?: time
        }
        return map
    }

    val markerVisibilityChangeListener = object : MarkerVisibilityChangeListener {
        override fun onMarkerShown(marker: Marker, markerEntryModels: List<Marker.EntryModel>) {
            markerEntryModels.forEach { entryModel ->
                Log.d("MarkerVisibility", "Marker shown with index: ${entryModel.index}")
            }
        }

        override fun onMarkerHidden(marker: Marker) {
            // Handle marker being hidden, if needed
        }

        override fun onMarkerMoved(marker: Marker, markerEntryModels: List<Marker.EntryModel>) {
            // Optional: Handle marker movement
        }
    }

    private fun convertTimeToCorrectFormat(time: Int, maxTime:Int): Float {
        return if(maxTime < 60){
            time.toFloat()
        } else if(maxTime < 3600){
            time.toFloat() / 60
        } else{
            time.toFloat() / 3600
        }
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