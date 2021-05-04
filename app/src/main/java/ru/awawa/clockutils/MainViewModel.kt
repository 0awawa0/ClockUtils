package ru.awawa.clockutils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ru.awawa.clockutils.helper.Stopwatch
import java.util.*
import kotlin.concurrent.timer

class MainViewModel: ViewModel() {

    val currentTime = Stopwatch.time
    val isRunning = Stopwatch.isRunning

    fun onStartStopwatch() {
        Stopwatch.start()
    }

    fun onPauseStopwatch() {
        Stopwatch.pause()
    }

    fun onStopStopwatch() {
        Stopwatch.stop()
    }
}