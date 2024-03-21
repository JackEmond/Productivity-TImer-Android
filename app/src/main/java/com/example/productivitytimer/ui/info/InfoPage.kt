package com.example.productivitytimer.ui.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.productivitytimer.ui.theme.ProductivityTimerTheme

@Composable
fun InfoPage(
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        ProductivityTimerText()
        TextContent()
    }
}

@Composable
fun ProductivityTimerText() {
    Text(
        text = "Productivity Timer",
        fontSize = 50.sp,
        textAlign = TextAlign.Center
    )
}


@Composable
fun TextContent() {
    Text(
        text = " \n Productivity Timer is a mobile app developed to allow you to become a more productive person." +
                "    \n\n" +
                "    Start a new timer by selecting where you are located and what task you will be completing. The timer will then run even when the app is closed! When you are done working on the task click save and your information will be saved and analyzed." +
                "    \n\n" +
                "    Go to the stats page and see how long you have been working on a task, what day you are most productive, how long you have spent doing each task, and how long you work at each location." +
                "    \n\n" +
                "    Use this information to figure out where your strengths and weaknesses are and become a more productive person! ",
        fontSize = 18.sp,
        style = MaterialTheme.typography.titleLarge,
    )

}


@Preview(showBackground = true)
@Composable
private fun InfoPagePreview() {
    ProductivityTimerTheme {
        InfoPage()
    }
}