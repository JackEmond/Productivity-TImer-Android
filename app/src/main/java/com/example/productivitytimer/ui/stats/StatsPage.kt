package com.example.productivitytimer.ui.stats

import android.content.res.Configuration
import android.text.Layout
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
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

@Composable
fun StatsPage(
    statsVM: StatsViewModel = hiltViewModel()
){
    val data by statsVM.graphData.observeAsState(initial = emptyMap())
    val timerRecords by statsVM.getAllTimers().observeAsState(initial = emptyList())
    val timeProductiveThatDay by statsVM.timeProductiveThatDay.observeAsState(initial = TimerRecord(id = 0, date="May-4th", timeInSeconds = 4))

    StatsPageContent(
        data = data,
        timerRecords = timerRecords,
        deleteTimer = { id-> statsVM.deleteTimer(id)},
        markerVisibilityChangeListener = statsVM.markerVisibilityChangeListener,
        timeProductiveThatDay = timeProductiveThatDay
        )
}

@Composable
fun StatsPageContent(
    data: Map<String, Int>,
    timerRecords: List<TimerRecord>,
    deleteTimer: (Int) -> Unit,
    markerVisibilityChangeListener: MarkerVisibilityChangeListener,
    timeProductiveThatDay: TimerRecord,
    ) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
    ){
        StatsText()
        VicoChart(data, markerVisibilityChangeListener)
        TimeProductiveCircle(timeProductiveThatDay)
        AllTimersText()
        AllTimers(timerRecords, deleteTimer = deleteTimer)
    }
}

@Composable
fun TimeProductiveCircle(timeProductiveThatDay: TimerRecord) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(Color(0xFF222226), CircleShape) // Apply the black background 
            .border(
                2.dp,
                Color.White,
                CircleShape
            ) // Then apply the red border over the background.
            .padding(10.dp)
    ){
        Text(
            text = "Time Productive - ${timeProductiveThatDay.date}",
            color =  Color.White,
            fontSize =  14.sp
        )
        Text(
            text = timeProductiveThatDay.getFormattedTime(),
            color =  Color.White,
            fontSize =  26.sp
        )
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
    data: Map<String, Int>,
    markerVisibilityChangeListener: MarkerVisibilityChangeListener
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 40.dp)) { //This allows the background and foreground to be separate
        Column(modifier = Modifier.matchParentSize()
        ) { // This is the background
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
    data: Map<String, Int>,
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
                text = timerRecord.getFormattedTime(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Black)

            Text(
                text = timerRecord.date,
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
fun AllTimersText() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 35.dp)
        ,
        fontSize = 50.sp,
        text = "All  Timers",
        textAlign = TextAlign.Center,
    )
}


@Preview(apiLevel = 33, showBackground = true)
@Composable
private fun StatsPagePreview() {
    PreviewContent()
}

@Preview(apiLevel = 33, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun StatsPagePreviewDarkMode() {
    PreviewContent()
}

@Composable
private fun PreviewContent() {
    ProductivityTimerTheme { Surface {
        StatsPageContent(
            data = mapOf(
                "Mon" to 2,
                "Tue" to 9,
                "Wed" to 3,
                "Thu" to 3,
                "Fri" to 4,
                "Sat" to 3,
            ),
            timerRecords = listOf(
                TimerRecord(1, "02-07-2024", 412),
                TimerRecord(1, "03-23-2024", 336),
                TimerRecord(1, "03-24-2024", 512),
            ),
            deleteTimer = {},
            markerVisibilityChangeListener = EmptyMarkerVisibilityChangeListener,
            timeProductiveThatDay = TimerRecord(1, "02-07-2024", 412),
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