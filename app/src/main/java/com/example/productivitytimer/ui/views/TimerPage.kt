package com.example.productivitytimer.ui.views

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

    val formattedTime by timerVM.formattedTime.collectAsState()
    val pauseOrResumeTimer = { timerVM.pauseOrResumeTimer() }
    val cancelTimer = {
        timerVM.cancelTimer()
        navigateToCreateTimerPage()
    }
    val saveTimer = {
        timerVM.saveTimer()
        navigateToCreateTimerPage()
    }
    val isPaused by timerVM.timerPaused.observeAsState(false)

    TimerPageScreen(
        formattedTime = formattedTime,
        pauseOrResumeTimer = pauseOrResumeTimer,
        cancelTimer = cancelTimer,
        saveTimer = saveTimer,
        isPaused = isPaused
    )

}

@Composable
fun TimerPageScreen(
    formattedTime: String,
    pauseOrResumeTimer: () -> Unit,
    cancelTimer: () -> Unit,
    saveTimer: () -> Unit,
    isPaused: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        HeaderText()
        TimerText(formattedTime)
        Buttons(
            pauseOrResumeTimer = pauseOrResumeTimer,
            cancelTimer = cancelTimer,
            saveTimer = saveTimer,
            isPaused = isPaused
        )
    }
}


@Composable
private fun Buttons(
    pauseOrResumeTimer: () -> Unit,
    cancelTimer: () -> Unit,
    saveTimer: () -> Unit,
    isPaused: Boolean,
) {
    Row{
        TimerButton(
            text = if (isPaused) "Resume" else "Pause",
            onClick = pauseOrResumeTimer
        )

        TimerButton(
            text = "Cancel",
            onClick = cancelTimer
        )

        TimerButton(
            text = "Save",
            onClick = saveTimer
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
private fun TimerText(formattedTime: String) {
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

@Preview(showBackground = true)
@Composable
private fun TimerPagePreview() {
    ProductivityTimerTheme {
        TimerPageScreen(
            formattedTime = "01HRS 30MIN 20SEC",
            pauseOrResumeTimer = {},
            cancelTimer = {},
            saveTimer = {},
            isPaused = false
        )

    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TimerPagePreviewDarkMode() {
    ProductivityTimerTheme { Surface {
        TimerPageScreen(
            formattedTime = "01HRS 30MIN 20SEC",
            pauseOrResumeTimer = {},
            cancelTimer = {},
            saveTimer = {},
            isPaused = false
        )
    }
    }
}