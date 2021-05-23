package ru.awawa.clockutils

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ru.awawa.clockutils.service.StopwatchService
import ru.awawa.clockutils.service.TimerService
import ru.awawa.clockutils.ui.theme.ClockUtilsTheme
import ru.awawa.clockutils.ui.theme.Grey500
import ru.awawa.clockutils.ui.views.BottomBar
import ru.awawa.clockutils.ui.views.NavigationItem
import ru.awawa.clockutils.ui.views.StopwatchView
import ru.awawa.clockutils.ui.views.TimerView

class MainActivity : ComponentActivity() {

    companion object {
        const val EXTRA_TYPE = "TYPE"
        const val TYPE_STOPWATCH = "Stopwatch"
        const val TYPE_TIMER = "Timer"
    }

    private val viewModel by viewModels<MainViewModel>()
    private var selectedItem by mutableStateOf(0)
    private val navigationItems = listOf(
        NavigationItem.Stopwatch,
        NavigationItem.Timer,
        NavigationItem.Alarm
    )
    private val currentNavItem: NavigationItem
        get() = navigationItems[selectedItem]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (intent.getStringExtra(EXTRA_TYPE)) {
            TYPE_STOPWATCH -> selectedItem = 0
            TYPE_TIMER -> selectedItem = 1
        }

        setContent {
            val navController = rememberNavController()
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
                        val currentStopwatchTime: Long by viewModel.currentStopwatchTime.collectAsState()
                        val isStopwatchRunning: Boolean by viewModel.isStopwatchRunning.collectAsState()
                        val currentTimerTime: Long by viewModel.currentTimerTime.collectAsState()
                        val isTimerRunning: Boolean by viewModel.isTimerRunning.collectAsState()

                        NavHost(
                            navController = navController,
                            startDestination = currentNavItem.route,
                            modifier = Modifier
                                .padding(paddings)
                                .fillMaxSize()
                        ) {
                            for (item in navigationItems) {
                                composable(item.route) {
                                    when (item.route) {
                                        NavigationItem.Stopwatch.route -> StopwatchView(
                                            label = getString(R.string.stopwatch),
                                            currentTime = currentStopwatchTime,
                                            isRunning = isStopwatchRunning,
                                            onStartStopwatch = viewModel::onStartStopwatch,
                                            onPauseStopwatch = viewModel::onPauseStopwatch,
                                            onStopStopwatch = viewModel::onStopStopwatch
                                        )
                                        NavigationItem.Timer.route -> TimerView(
                                            label = getString(R.string.timer),
                                            currentTime = currentTimerTime,
                                            isRunning = isTimerRunning,
                                            onSetTime = viewModel::onSetTimerTime,
                                            onStartTimer = { viewModel.onStartTimer(this@MainActivity) },
                                            onPauseTimer = viewModel::onPauseTimer,
                                            onStopTimer = viewModel::onStopTimer
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

    override fun onPause() {
        super.onPause()

        if (viewModel.isStopwatchRunning.value) {
            ContextCompat.startForegroundService(
                this,
                Intent(this, StopwatchService::class.java)
            )
        }

        if (viewModel.isTimerRunning.value) {
            ContextCompat.startForegroundService(
                this,
                Intent(this, TimerService::class.java)
            )
        }
    }

    override fun onResume() {
        super.onResume()

        stopService(Intent(this, StopwatchService::class.java))
        stopService(Intent(this, TimerService::class.java))
    }
}