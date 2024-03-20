package com.example.productivitytimer.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.productivitytimer.ui.ProductivityTimerViewModel
import com.example.productivitytimer.ui.TimerRecord
import com.example.productivitytimer.ui.theme.ProductivityTimerTheme
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.CartesianChartHost
import com.patrykandpatrick.vico.compose.chart.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.chart.rememberCartesianChart
import com.patrykandpatrick.vico.compose.component.rememberLineComponent
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.ExtraStore
import com.patrykandpatrick.vico.core.model.columnSeries
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AllTimersPage(
    timerVM: ProductivityTimerViewModel
){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
      ){
        StatsText()
        VicoChart(timerVM)
        AllTimersText()
        AllTimers(timerVM)
    }
}

@Composable
fun StatsText() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(top = 35.dp, bottom = 35.dp)
        ,
        fontSize = 45.sp,
        color = Color.White,
        text = "Stats",
        textAlign = TextAlign.Center,
    )
}


@Composable
fun VicoChart(timerVM: ProductivityTimerViewModel) {
    Box(modifier = Modifier
        .height(200.dp)
        .fillMaxWidth()) {
        Column(modifier = Modifier.matchParentSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.Black)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.White)
            )
        }
            // This box will fill the bottom half of the container
        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth(0.9f)
                .align(Alignment.Center)
        ){
            val modelProducer = remember { CartesianChartModelProducer.build() }
            val data2 by timerVM.graphData.observeAsState(initial = emptyMap())
            LaunchedEffect(data2) {
                modelProducer.tryRunTransaction {
                    columnSeries {
                        series(data2.values)
                    }
                }
            }
            val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
            val bottomAxisValueFormatter =
                AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _, _ -> daysOfWeek[x.toInt() % daysOfWeek.size] }

            CartesianChartHost(
                rememberCartesianChart(
                    rememberColumnCartesianLayer(
                        listOf(
                            rememberLineComponent(
                                color = Color.Black,
                                thickness = 12.dp,
                                shape = Shapes.roundedCornerShape(allPercent = 40),
                            )
                        )
                    ),
                    startAxis = rememberStartAxis(),
                    bottomAxis = rememberBottomAxis(
                        valueFormatter = bottomAxisValueFormatter
                    ),
                ),
                modelProducer,
            )
        }
    }
}

@Composable
fun AllTimers(timerVM: ProductivityTimerViewModel) {
    val timerRecords by timerVM.getAllTimers().observeAsState(initial = emptyList())
    LazyColumn{
        items(timerRecords) { timerRecord ->
            DisplayTimer(timerRecord,timerVM)
        }
    }
}

@Composable
fun DisplayTimer(timerRecord: TimerRecord, timerVM: ProductivityTimerViewModel) {
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
            onClick = { timerVM.deleteTimer(timerRecord.id) },
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
private fun AllTimersPagePreview() {
    ProductivityTimerTheme {
        AllTimersText()
    }
}
@Preview(apiLevel = 33, showBackground = true)
@Composable
private fun VicoChartPreview() {
    ProductivityTimerTheme {
        //VicoChart()
        //DisplayTimer(TimerRecord(3, 6,"123"), timerVM)
    }
}