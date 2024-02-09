package com.example.productivitytimer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.productivitytimer.ui.theme.ProductivityTimerTheme
import com.example.productivitytimer.viewmodel.TimerViewModel


@Composable
fun TimerPage(
    navController: NavHostController,
    timerViewModel: TimerViewModel = viewModel()
){
    //When app starts, start the timer
    LaunchedEffect(key1 = true) {
        timerViewModel.startTimer()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        HeaderText()
        TimerText(timerViewModel.time)
        Buttons(navController, timerViewModel)
    }
}


@Composable
private fun Buttons(
    navController: NavHostController, timerViewModel: TimerViewModel,
) {
    Row(){
        //TimerButton(text = "Pause", onClick = { timerViewModel.pauseTimer()})

        TimerButton(
            text = "Cancel",
            onClick = {
                timerViewModel.cancelTimer()
                navController.navigate("CreateTimerPage")
            }
        )

        TimerButton(text = "Save", onClick = { timerViewModel.saveTimer()})
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
        colors = ButtonDefaults.buttonColors(Color.Black),
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
private fun TimerText(time: Int) {
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

fun formatTime(time: Int): String {
    val hours = time / 3600
    val minutes = (time % 3600) / 60
    val seconds = time % 60

    return String.format("%02dHRS %02dMIN %02dSEC", hours, minutes, seconds)
}

@Preview(showBackground = true)
@Composable
private fun TimerPagePreview() {
    ProductivityTimerTheme {
        TimerPage(navController = rememberNavController())
    }
}