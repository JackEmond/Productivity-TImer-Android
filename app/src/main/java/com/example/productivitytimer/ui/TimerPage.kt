package com.example.productivitytimer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.productivitytimer.ui.theme.ProductivityTimerTheme


@Composable
fun TimerPage(
    navController: NavHostController,
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        ProductivityTimerText()
        TimerText()
    }
}



@Composable
fun ProductivityTimerText() {
    Text(
        text = "Productivity Timer",
        fontSize = 40.sp,
        textAlign = TextAlign.Center
    )
}

@Composable
fun TimerText() {
    Text(
        text = "00hHRS 00MIN 00SEC",
        textAlign = TextAlign.Center,
        fontSize = 20.sp
    )
}

@Preview(showBackground = true)
@Composable
fun TimerPagePreview() {
    ProductivityTimerTheme {
        //TimerPage(navController = NavHostController(this))
    }
}