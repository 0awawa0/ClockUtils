package ru.awawa.clockutils

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ru.awawa.clockutils.ui.theme.ClockUtilsTheme
import ru.awawa.clockutils.ui.theme.Grey700
import ru.awawa.clockutils.ui.views.*


class MainActivity : ComponentActivity() {

    companion object {
        const val EXTRA_TYPE = "TYPE"
        const val TYPE_STOPWATCH = "Stopwatch"
        const val TYPE_TIMER = "Timer"
    }

    private val tag = "MainActivity"

    private val viewModel by viewModels<MainViewModel>()
    private var selectedItem by mutableStateOf(0)
    private val navigationItems = listOf(NavigationItem.Stopwatch, NavigationItem.Timer, NavigationItem.Metronome)
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
                    color = Grey700,
                    darkIcons = false
                )
                Scaffold(
                    bottomBar = { BottomBar(
                        items = navigationItems,
                        currentItem = currentNavItem,
                        onItemSelected = {
                            if (selectedItem != it) {
                                selectedItem = it
                                navController.navigate(currentNavItem.route)
                            }
                        }
                    ) },
                    content = { paddings ->
                        val currentStopwatchTime: Long by viewModel.currentStopwatchTime.collectAsState()
                        val isStopwatchRunning: Boolean by viewModel.isStopwatchRunning.collectAsState()
                        val currentTimerTime: Long by viewModel.currentTimerTime.collectAsState()
                        val totalTimerTime: Long by viewModel.totalTimerTime.collectAsState()
                        val isTimerRunning: Boolean by viewModel.isTimerRunning.collectAsState()
                        val bitsPerMinute: Float by viewModel.bitsPerMinute.collectAsState()

                        NavHost(
                            navController = navController,
                            startDestination = currentNavItem.route,
                            modifier = Modifier
                                .padding(paddings)
                                .fillMaxSize()
                        ) {
                            val childModifier = Modifier.fillMaxSize()
                            for (item in navigationItems) {
                                composable(item.route) {
                                    when (item.route) {
                                        NavigationItem.Stopwatch.route -> StopwatchView(
                                            modifier = childModifier,
                                            currentTime = currentStopwatchTime,
                                            isRunning = isStopwatchRunning,
                                            viewModel.checkPoints.value,
                                            onSwitchStopwatch = viewModel::onSwitchStopwatch,
                                            onResetStopwatch = viewModel::onResetStopwatch,
                                            onAddCheckPoint = viewModel::onAddCheckPoint
                                        )

                                        NavigationItem.Timer.route -> TimerView(
                                            label = "",
                                            currentTime = currentTimerTime,
                                            totalTime = totalTimerTime,
                                            isRunning = isTimerRunning,
                                            onSetTime = viewModel::onSetTimerTime,
                                            onStartTimer = { viewModel.onStartTimer(applicationContext) },
                                            onPauseTimer = viewModel::onPauseTimer,
                                            onStopTimer = viewModel::onStopTimer
                                        )

                                        NavigationItem.Metronome.route -> RoundRangePickView(
                                            modifier = childModifier.padding(16.dp),
                                            skipAngle = 45f,
                                            minValue = 10f,
                                            maxValue = 300f,
                                            startValue = 60f,
                                            onValueChanged = {  viewModel.onBpmChanged(bitsPerMinute) },
                                            textFormatter = { String.format("%02d", (290 * it + 10).toInt()) },
                                            onButtonClick = { viewModel.onBpmChanged(bitsPerMinute - 2) }
                                        )
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
    }

    override fun onResume() {
        super.onResume()
        Log.w(tag, "onResume()")
    }
}