package ru.awawa.clockutils

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ru.awawa.clockutils.ui.theme.ClockUtilsTheme
import ru.awawa.clockutils.ui.theme.Grey500
import ru.awawa.clockutils.ui.views.BottomBar
import ru.awawa.clockutils.ui.views.NavigationItem

class MainActivity : ComponentActivity() {

    private var selectedItem by mutableStateOf(0)
    private val navigationItems = listOf(
        NavigationItem("stopwatch", Icons.Default.Timer),
        NavigationItem("timer", Icons.Default.AvTimer)
    )
    private val currentNavItem: NavigationItem
        get() = navigationItems[selectedItem]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val navigationItems = listOf(
                NavigationItem("stopwatch", Icons.Default.Timer),
                NavigationItem("timer", Icons.Default.AvTimer)
            )
            val systemUiController = rememberSystemUiController()
            ClockUtilsTheme {
                systemUiController.setSystemBarsColor(
                    color = Grey500,
                    darkIcons = false
                )
                // A surface container using the 'background' color from the theme
                Scaffold(
                    bottomBar = { BottomBar(
                        items = navigationItems,
                        currentItem = currentNavItem,
                        onItemSelected = {
                            selectedItem = it
                            navController.navigate(currentNavItem.route)
                        }
                    ) }
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = navigationItems[0].route
                    ) {
                        for (item in navigationItems) {
                            composable(item.route) {
                                Content(item.route)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Content(text: String) {
    Text(text = text)
}