package com.example.productivitytimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.productivitytimer.ui.theme.ProductivityTimerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProductivityTimerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        CreateProductivityTimerText()
        TaskCompletingInput()
        LocationInput()
        SubmitButton()
    }
}



@Composable
fun CreateProductivityTimerText(){
    Text(
        modifier = Modifier.padding(10.dp),
        text = "Create Productivity Timer",
        fontFamily = FontFamily(Font(R.font.bebas_neue)),
        textAlign = TextAlign.Center,
        fontSize = 50.sp,
        lineHeight = 60.sp,
        fontWeight = FontWeight.ExtraBold,
    )
}

@Composable
fun TaskCompletingInput() {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = {text = it},
        label = { Text("Task Completing") }
    )
}

@Composable
fun LocationInput() {
    var text by remember { mutableStateOf("") }

    TextField(
        modifier = Modifier.padding(top = 10.dp),
        value = text,
        onValueChange = {text = it},
        label = { Text("Location") }
    )
}

@Composable
fun SubmitButton() {
    Button(
        onClick = {},
        Modifier.padding(top = 10.dp),
        colors = ButtonDefaults.buttonColors(Color.Black),
        shape = RectangleShape,
    ) {
        Text("Start Productive Work")
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProductivityTimerTheme {
        Greeting("Android")
    }
}