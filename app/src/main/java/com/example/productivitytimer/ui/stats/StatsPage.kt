package com.example.productivitytimer.ui.stats

import android.content.res.Configuration
import android.text.Layout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.productivitytimer.ui.timer.TimerRecord
import com.example.productivitytimer.ui.theme.ProductivityTimerTheme
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.CartesianChartHost
import com.patrykandpatrick.vico.compose.chart.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.chart.rememberCartesianChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.component.marker.rememberMarkerComponent
import com.patrykandpatrick.vico.compose.component.rememberLineComponent
import com.patrykandpatrick.vico.core.chart.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.text.TextComponent
import com.patrykandpatrick.vico.core.marker.Marker
import com.patrykandpatrick.vico.core.marker.MarkerVisibilityChangeListener
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.columnSeries
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun StatsPage(
    statsVM: StatsViewModel = hiltViewModel()
){
    val data by statsVM.graphData.observeAsState(initial = emptyMap())
    val timerRecords by statsVM.getAllTimers().observeAsState(initial = emptyList())
    StatsPageContent(
        data = data,
        timerRecords = timerRecords,
        deleteTimer = { id-> statsVM.deleteTimer(id)},
        markerVisibilityChangeListener = statsVM.markerVisibilityChangeListener,
        )
}

@Composable
fun StatsPageContent(
    data: Map<String, Float>,
    timerRecords: List<TimerRecord>,
    deleteTimer: (Int) -> Unit,
    markerVisibilityChangeListener: MarkerVisibilityChangeListener,
    ) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
    ){
        StatsText()
        VicoChart(data, markerVisibilityChangeListener)
        AllTimersText()
        AllTimers(timerRecords, deleteTimer = deleteTimer)
    }
}

@Composable
fun StatsText() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(top = 35.dp, bottom = 35.dp)
        ,
        fontSize = 45.sp,
        color = MaterialTheme.colorScheme.secondary,
        text = "Stats",
        textAlign = TextAlign.Center,
    )
}

@Composable
fun VicoChart(
    data: Map<String, Float>,
    markerVisibilityChangeListener: MarkerVisibilityChangeListener
) {
    Box(modifier = Modifier.fillMaxWidth()) { //This allows the background and foreground to be separate
        Column(modifier = Modifier.matchParentSize()) { // This is the background
            Box( // This is the background of the top half of the graph
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
            )
            Box( // This is the background of the bottom half of the graph
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondary)
            )
        }
        Box( //This  is the graph
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondary)
                .fillMaxWidth(0.9f)
                .align(Alignment.Center)
        ) {
            StatsGraph(data, markerVisibilityChangeListener)
        }
    }
}

@Composable
fun StatsGraph(
    data: Map<String, Float>,
    markerVisibilityChangeListener: MarkerVisibilityChangeListener
) {
    val daysOfWeek = data.keys.toList()
    val values = data.values

    //Set up model producer
    val modelProducer = remember { CartesianChartModelProducer.build() }
    if (LocalInspectionMode.current) {  modelProducer.tryRunTransaction { columnSeries { series(values) } } }
    LaunchedEffect(data){ modelProducer.tryRunTransaction { columnSeries { series(values) } } }

    val myLabel = TextComponent.build {
        textSizeSp = 16f
        textAlignment = Layout.Alignment.ALIGN_CENTER
    }

    val marker = rememberMarkerComponent(label =  myLabel)

    //Display the Graph
    CartesianChartHost(
    markerVisibilityChangeListener= markerVisibilityChangeListener,
    scrollState = rememberVicoScrollState(scrollEnabled = true),
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(
                ColumnCartesianLayer.ColumnProvider.series(
                    rememberLineComponent(
                        color = MaterialTheme.colorScheme.primary,
                        thickness = 12.dp,
                        shape = Shapes.roundedCornerShape(allPercent = 40),
                    ),
                )
            ),
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(
                valueFormatter = { x, _, _ -> daysOfWeek[x.toInt() % daysOfWeek.size] }
            ),
        ),
        modelProducer =  modelProducer,
        marker = marker,
    )
}

@Composable
fun AllTimers(timerRecords: List<TimerRecord>, deleteTimer: (Int) -> Unit) {
    LazyColumn{
        items(timerRecords) { timerRecord ->
            DisplayTimer(timerRecord = timerRecord, deleteTimer = {deleteTimer(timerRecord.id)})
        }
    }
}

@Composable
fun DisplayTimer(timerRecord: TimerRecord, deleteTimer:() -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth(.7f)
            .padding(6.dp)
    ){
        Column{
            Text(
                text = getFormattedTime(timerRecord.time),
                fontSize = 20.sp,
                fontWeight = FontWeight.Black)

            Text(
                text = getFormattedDate(timerRecord.date),
                fontSize = 12.sp
            )
        }
        Button(
            onClick = deleteTimer,
            shape = RectangleShape
        ) {
            Text(text = "Delete")
        }
    }
}

@Composable
fun getFormattedTime(time: Int): String {
    return remember(time){
        val hours = time / 3600
        val minutes = (time % 3600) / 60
        val seconds = time % 60

        when {
            hours > 0 -> String.format("%02d HRS %02d MIN %02d SEC", hours, minutes, seconds)
            minutes > 0 -> String.format("%02d MIN %02d SEC", minutes, seconds)
            else -> String.format("%02d SEC", seconds)
        }
    }
}


@Composable
fun getFormattedDate(date: Long): String {
    return remember(date) {
        val formatter = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
        formatter.format(Date(date)).toString()
    }
}

@Composable
fun AllTimersText() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(35.dp)
        ,
        fontSize = 50.sp,
        text = "All Timers",
        textAlign = TextAlign.Center,
    )
}

@Preview(apiLevel = 33, showBackground = true)
@Composable
private fun StatsPagePreview() {
    ProductivityTimerTheme {
        StatsPageContent(
            data = mapOf(
                "Mon" to 2f,
                "Tue" to 9f,
                "Wed" to 3f,
                "Thu" to 3f,
                "Fri" to 4f,
                "Sat" to 3f,
            ),
            timerRecords =  listOf(
                TimerRecord(1, 50, 100),
                TimerRecord(1, 312, 100),
                TimerRecord(1, 978, 100),
            ),
            deleteTimer =  {},
            markerVisibilityChangeListener =  EmptyMarkerVisibilityChangeListener,
        )
    }
}

@Preview(apiLevel = 33, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun StatsPagePreviewDarkMode() {
    ProductivityTimerTheme { Surface{
        StatsPageContent(
            data = mapOf(
                "Sun" to 1f,
                "Mon" to 2f,
                "Tue" to 9f,
                "Wed" to 3f,
                "Thu" to 3f,
                "Fri" to 4f,
                "Sat" to 3f,
            ),
            timerRecords =  listOf(
                TimerRecord(1, 50, 100),
                TimerRecord(1, 312, 100),
                TimerRecord(1, 978, 100),
            ),
            deleteTimer =  {},
            markerVisibilityChangeListener = EmptyMarkerVisibilityChangeListener,
        )
    }
    }
}

//Interface needs to be passed for MarkerVisibilityChangeListener in graph so this allows the preview to set the default
object EmptyMarkerVisibilityChangeListener : MarkerVisibilityChangeListener {
    override fun onMarkerShown(marker: Marker, markerEntryModels: List<Marker.EntryModel>) {}
    override fun onMarkerHidden(marker: Marker) {}
    override fun onMarkerMoved(marker: Marker, markerEntryModels: List<Marker.EntryModel>) {}
}