package com.example.productivitytimer.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.productivitytimer.ui.ProductivityTimerViewModel
import com.example.productivitytimer.ui.theme.ProductivityTimerTheme


@Composable
fun TimerPage(
    navigateToCreateTimerPage: () -> Unit,
    timerVM: ProductivityTimerViewModel
){
    //When app starts, start the timer
    LaunchedEffect(Unit) {
        timerVM.startTimer()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        HeaderText()

        TimerText(timerVM.time.collectAsState())
        Buttons(navigateToCreateTimerPage, timerVM)
    }
}


@Composable
private fun Buttons(
    navigateToCreateTimerPage: () -> Unit,
    timerVM: ProductivityTimerViewModel,
) {
    val isPaused by timerVM.timerPaused.observeAsState(false)

    Row{
        TimerButton(
            text = if (isPaused) "Resume" else "Pause",
            onClick = {
                timerVM.pauseOrResumeTimer()
            }
        )

        TimerButton(
            text = "Cancel",
            onClick = {
                timerVM.cancelTimer()
                navigateToCreateTimerPage()
            }
        )

        TimerButton(
            text = "Save",
            onClick = {
                timerVM.saveTimer()
                navigateToCreateTimerPage()
            }
        )
    }
}

@Composable
private fun TimerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier.padding(10.dp),
        shape = RectangleShape,
    ) {
        Text(
            text = text.uppercase(),
            Modifier.padding(5.dp),
            fontSize = 15.sp
        )
    }
}


@Composable
private fun HeaderText() {
    Text(
        text = "Productivity Timer",
        fontSize = 50.sp,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun TimerText(time: State<Int>) {
    val formattedTime = formatTime(time)

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(35.dp)
        ,
        fontSize = 40.sp,
        text = formattedTime,
        color = Color.White,
        textAlign = TextAlign.Center,
    )
}

fun formatTime(time: State<Int>): String {

    val hours = time.value / 3600
    val minutes = (time.value % 3600) / 60
    val seconds = time.value % 60

    return String.format("%02dHRS %02dMIN %02dSEC", hours, minutes, seconds)
}

@Preview(showBackground = true)
@Composable
private fun TimerPagePreview() {
    ProductivityTimerTheme {
        //TimerPage(navController = rememberNavController())
    }
}