package com.example.productivitytimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.productivitytimer.ui.BottomNavBar
import com.example.productivitytimer.ui.timer.CreateTimerPage
import com.example.productivitytimer.ui.views.InfoPage
import com.example.productivitytimer.ui.timer.ProductivityTimerViewModel
import com.example.productivitytimer.ui.stats.StatsPage
import com.example.productivitytimer.ui.stats.StatsViewModel
import com.example.productivitytimer.ui.timer.TimerPage
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
fun MyNavController(navController: NavHostController, timerVM: ProductivityTimerViewModel)
{
    val statsVM: StatsViewModel = hiltViewModel()
    NavHost(navController = navController, startDestination = "CreateTimerPage") {
        composable("CreateTimerPage") {
            CreateTimerPage( navigateToTimerPage = {navController.navigate("TimerPage")}, timerVM) }
        composable("TimerPage") {
            TimerPage(navigateToCreateTimerPage = {navController.navigate("CreateTimerPage")}, timerVM) }
        composable("AllTimersPage") { StatsPage(statsVM) }
        composable("InfoPage") { InfoPage() }
    }
}
