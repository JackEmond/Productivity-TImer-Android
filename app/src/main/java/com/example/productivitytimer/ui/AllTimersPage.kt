package com.example.productivitytimer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.productivitytimer.ui.theme.ProductivityTimerTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AllTimersPage(
    timerVM: ProductivityTimerViewModel = hiltViewModel()
){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
      ){
        AllTimersText()
        AllTimers(timerVM)
    }
}

@Composable
fun AllTimers(timerVM: ProductivityTimerViewModel) {
    val timerRecords by timerVM.getAllTimers().observeAsState(initial = emptyList())
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ){
        items(timerRecords) { timerRecord ->
            DisplayTimer(timerRecord,timerVM)
        }
    }
}

@Composable
fun DisplayTimer(timerRecord: TimerRecord, timerVM: ProductivityTimerViewModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ){
        Column{
            Text(text = timerRecord.time.toString() + " sec",
                fontSize = 20.sp, fontWeight = FontWeight.Black)
            Text(text = getFormattedDate(timerRecord.date), fontSize = 12.sp)
        }
        Button(onClick = { timerVM.deleteTimer(timerRecord.id) }) {
            Text(text = "Delete")
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
        AllTimersPage()
    }
}
@Preview(apiLevel = 33, showBackground = true)
@Composable
private fun DisplayTImerPreview() {
    ProductivityTimerTheme {
        //DisplayTimer(TimerRecord(3, 6), timerVM)
    }
}