package com.example.productivitytimer.ui.pages

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    val timerVariable = timerVM.time.collectAsState()
    LaunchedEffect(timerVariable) {
        Log.w("Jack", timerVariable.value.toString())
        if(timerVariable.value > 0)
            navigateToTimerPage()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        CreateProductivityTimerText()
        SubmitButton{ navigateToTimerPage()}
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
fun SubmitButton(navigate: () -> Unit) {
    Button(
        onClick = navigate,
        Modifier.padding(top = 20.dp),

        colors = ButtonDefaults.buttonColors(Color.Black),
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
        //CreateTimerPage(navHostController = rememberNavController())
    }
}