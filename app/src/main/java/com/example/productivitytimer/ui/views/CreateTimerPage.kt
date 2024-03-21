package com.example.productivitytimer.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.productivitytimer.ui.ProductivityTimerViewModel
import com.example.productivitytimer.ui.theme.ProductivityTimerTheme


@Composable
fun CreateTimerPage(
    navigateToTimerPage: () -> Unit,
    timerVM: ProductivityTimerViewModel
) {
    NavigatedToTimerIfRunning(timerVM = timerVM, navigateToTimerPage = navigateToTimerPage)
    CreateTimerScreen(startTimer = {timerVM.startTimerInitial()})
}

@Composable
fun CreateTimerScreen(startTimer: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        CreateProductivityTimerText()
        SubmitButton(startTimer)
    }
}

@Composable
fun NavigatedToTimerIfRunning(
    timerVM: ProductivityTimerViewModel,
    navigateToTimerPage: () -> Unit
) {
    val time = timerVM.time.collectAsState().value
    LaunchedEffect(time) {
        if (time > 0) {
            navigateToTimerPage()
        }
    }
}


@Composable
fun CreateProductivityTimerText(){
    Text(
        modifier = Modifier.padding(10.dp),
        text = "Create Productivity Timer",
        textAlign = TextAlign.Center,
        fontSize = 50.sp,
        lineHeight = 60.sp,
        fontWeight = FontWeight.ExtraBold,
    )
}


@Composable
fun SubmitButton(onclick: () -> Unit) {
    Button(
        onClick = onclick,
        Modifier.padding(top = 20.dp),
        shape = RectangleShape,
    ) {
        Text(text = "Start Productive Work",
            Modifier.padding(10.dp),
            fontSize = 16.sp)
    }
}


@Preview(showBackground = true)
@Composable
fun CreateTimerPagePreview() {
    ProductivityTimerTheme {
        CreateTimerScreen(startTimer = {})
    }
}