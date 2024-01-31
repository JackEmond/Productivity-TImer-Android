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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
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
        Buttons{navController.navigate("CreateTimerPage")}
    }
}

@Composable
private fun Buttons(navigate: () -> Unit) {
    Row(){
        TimerButton(text = "Pause", onClick = { /*TODO*/ })
        TimerButton(text = "Cancel", onClick = navigate)
        TimerButton(text = "Save", onClick = { /*TODO*/ })
    }

}

@Composable
private fun TimerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
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
private fun ProductivityTimerText() {
    Text(
        text = "Productivity Timer",
        fontSize = 50.sp,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun TimerText() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(35.dp)
        ,
        fontSize = 40.sp,
        text = "00hHRS 00MIN 00SEC",
        color = Color.White,
        textAlign = TextAlign.Center,
    )
}

@Preview(showBackground = true)
@Composable
private fun TimerPagePreview() {
    ProductivityTimerTheme {
        TimerPage(navController = rememberNavController())
    }
}