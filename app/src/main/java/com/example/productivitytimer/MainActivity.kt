package com.example.productivitytimer

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.productivitytimer.data.RunningTimerRepository
import com.example.productivitytimer.ui.BottomNavBar
import com.example.productivitytimer.ui.timer.CreateTimerPage
import com.example.productivitytimer.ui.info.InfoPage
import com.example.productivitytimer.ui.timer.ProductivityTimerViewModel
import com.example.productivitytimer.ui.stats.StatsPage
import com.example.productivitytimer.ui.stats.StatsViewModel
import com.example.productivitytimer.ui.timer.TimerPage
import com.example.productivitytimer.ui.theme.ProductivityTimerTheme
import com.example.productivitytimer.ui.timer.DetermineTimerStatusPage
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
    NavHost(navController = navController, startDestination = "DetermineTimerPage") {
        composable("DetermineTimerPage") {
            DetermineTimerStatusPage( navigateToTimerPage = {navController.navigate("TimerPage")},{navController.navigate("CreateTimerPage")},  timerVM) }
        composable("CreateTimerPage") {
            CreateTimerPage( navigateToTimerPage = {navController.navigate("TimerPage")}) }
        composable("TimerPage") {
            TimerPage(navigateToCreateTimerPage = {navController.navigate("CreateTimerPage")}, timerVM) }
        composable("AllTimersPage") { StatsPage() }
        composable("InfoPage") { InfoPage() }
    }
}
