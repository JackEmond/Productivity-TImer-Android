package com.example.productivitytimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.productivitytimer.ui.AllTimersPage
import com.example.productivitytimer.ui.CreateTimerPage
import com.example.productivitytimer.ui.ProductivityTimerViewModel
import com.example.productivitytimer.ui.TimerPage
import com.example.productivitytimer.ui.theme.ProductivityTimerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val timerViewModel: ProductivityTimerViewModel = hiltViewModel()
            val navController = rememberNavController()
            ProductivityTimerTheme {
                Scaffold(
                    bottomBar = {
                        BottomNavBar(navController)
                    }
                ) {
                    MyNavController(navController = navController, timerVM = timerViewModel)
                }
            }
        }
    }
}


@Composable
fun MyNavController(navController: NavHostController, timerVM: ProductivityTimerViewModel) {
    NavHost(navController = navController, startDestination = "CreateTimerPage") {
        composable("CreateTimerPage") {
            CreateTimerPage( navigateToTimerPage = {navController.navigate("TimerPage")}, timerVM) }
        composable("TimerPage") {
            TimerPage(navigateToStatsPage = {navController.navigate("AllTimersPage")}, navigateToCreateTimerPage = {navController.navigate("CreateTimerPage")}, timerVM) }
        composable("AllTimersPage") {
            AllTimersPage() }

    }
}

data class BottomNavItem(
    val title: String,
    val icon: Painter,
    val navigation: String
)

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem(
            title = "Stats",
            navigation = "AllTimersPage",
            icon = painterResource(id = R.drawable.bar_chart)
        ),
        BottomNavItem(
            title = "Timer",
            navigation ="CreateTimerPage",
            icon = painterResource(id = R.drawable.timer)
        ),
        BottomNavItem(
            title = "Info",
            navigation ="CreateTimerPage",
            icon = painterResource(id = R.drawable.info)
        )
    )
    var selectedItemIndex by rememberSaveable { mutableStateOf(1) }

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index
                    navController.navigate(item.navigation)
                },
                label = {
                    Text(text = item.title)
                },
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.title,
                        tint = if(index == selectedItemIndex) Color.Black  else Color.Gray

                    )
                }
            )
        }
    }
}