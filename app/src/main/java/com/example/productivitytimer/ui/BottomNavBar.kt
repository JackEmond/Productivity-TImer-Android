package com.example.productivitytimer.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.productivitytimer.R


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
            navigation ="InfoPage",
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