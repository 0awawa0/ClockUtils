package ru.awawa.clockutils

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ru.awawa.clockutils.ui.theme.ClockUtilsTheme
import ru.awawa.clockutils.ui.theme.Grey500
import ru.awawa.clockutils.ui.views.BottomBar
import ru.awawa.clockutils.ui.views.NavigationItem
import ru.awawa.clockutils.ui.views.StopwatchView

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()
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
                Scaffold(
                    bottomBar = { BottomBar(
                        items = navigationItems,
                        currentItem = currentNavItem,
                        onItemSelected = {
                            selectedItem = it
                            navController.navigate(currentNavItem.route)
                        }
                    ) },
                    content = { paddings ->
                        NavHost(
                            navController = navController,
                            startDestination = navigationItems[0].route,
                            modifier = Modifier.padding(paddings).fillMaxSize()
                        ) {
                            for (item in navigationItems) {
                                composable(item.route) {
                                    when (item.route) {
                                        "stopwatch" -> StopwatchView(
                                            currentTime = viewModel.currentTime,
                                            isRunning = viewModel.isRunning,
                                            onStartStopwatch = viewModel::onStartStopwatch,
                                            onPauseStopwatch = viewModel::onPauseStopwatch,
                                            onStopStopwatch = viewModel::onStopStopwatch
                                        )
                                        else -> Text(item.route)
                                    }
                                }
                            }
                        }
                    },
                )
            }
        }
    }
}