package com.example.productivitytimer.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.productivitytimer.ui.theme.ProductivityTimerTheme

@Composable
fun AllTimersPage(
    navHostController: NavHostController
    //timerVM: ProductivityTimerViewModel = hiltViewModel()
){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
      ){
        AllTimersText()
        AllTimers()
    }
}

@Composable
fun AllTimers() {
    DisplayTimer()
}

@Composable
fun DisplayTimer(){
    Row(verticalAlignment = Alignment.CenterVertically,
    ){
        Column {
            Text(text = "6 sec",
                fontSize = 20.sp, fontWeight = FontWeight.Black)
            Text(text = "JUL-13-2023", fontSize = 12.sp)
        }
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Delete")
        }
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
        AllTimersPage(navHostController = rememberNavController())
    }
}
@Preview(apiLevel = 33, showBackground = true)
@Composable
private fun DisplayTImerPreview() {
    ProductivityTimerTheme {
        DisplayTimer()
    }
}