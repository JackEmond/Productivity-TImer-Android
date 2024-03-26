package com.example.productivitytimer.ui.timer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState

@Composable
fun DetermineTimerStatusPage(
    navigateToTimerPage: () -> Unit,
    navigateToCreateTimerPage: () -> Unit,
    timerVM: ProductivityTimerViewModel
){
    val timeElapsed = timerVM.time.collectAsState().value
    LaunchedEffect(timeElapsed) {
        if (timeElapsed > 0) {
            navigateToTimerPage()
        } else if(timeElapsed == 0){
            navigateToCreateTimerPage()
        }
    }
}