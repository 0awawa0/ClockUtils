package ru.awawa.clockutils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.concurrent.timer

class MainViewModel: ViewModel() {

    private val timerInterval = 23L
    var currentTime by mutableStateOf(0L)
        private set

    val isRunning: Boolean
        get() = timer != null

    private var timer: Timer? by mutableStateOf(null)

    fun onStartStopwatch() {
        timer = timer(initialDelay = 0L, period = timerInterval) { currentTime += timerInterval }
    }

    fun onPauseStopwatch() {
        timer?.cancel()
        timer?.purge()
        timer = null
    }

    fun onStopStopwatch() {
        timer?.cancel()
        timer?.purge()
        timer = null
        currentTime = 0L
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
        timer?.purge()
        timer = null
    }
}