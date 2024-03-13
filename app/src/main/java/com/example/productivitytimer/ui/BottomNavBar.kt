package com.example.productivitytimer.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.productivitytimer.R
import com.example.productivitytimer.ui.theme.ProductivityTimerTheme


@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = getItems()
    var selectedItemIndex by rememberSaveable { mutableStateOf(1) }

    NavigationBar(
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.secondary),
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index
                    navController.navigate(item.navigation)
                },
                label = {
                    Text(
                        text = item.title,
                    )
                },
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.title,
                    )
                }
            )
        }
    }
}

data class BottomNavItem(
    val title: String,
    val icon: Painter,
    val navigation: String
)

@Composable
fun getItems(): List<BottomNavItem> {
    return listOf(
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
}

@Preview(apiLevel = 33, showBackground = true)
@Composable
private fun BottomNavBarPreview() {
    ProductivityTimerTheme {
        BottomNavBar(navController = rememberNavController())
    }
}