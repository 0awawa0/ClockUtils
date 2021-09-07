package ru.awawa.clockutils

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ru.awawa.clockutils.helper.StopwatchObj
import ru.awawa.clockutils.helper.TimerObj

class MainViewModel: ViewModel() {

    val currentStopwatchTime = StopwatchObj.time
    val isStopwatchRunning = StopwatchObj.isRunning

    data class CheckPoint(
        val timestamp: Long,
        var isVisible: Boolean = true
    )

    private val _checkPoints = LinkedHashSet<CheckPoint>()
    val checkPoints = mutableStateOf(_checkPoints)
    val currentTimerTime = TimerObj.time
    val isTimerRunning = TimerObj.isRunning

    fun onStartStopwatch() {
        if (currentStopwatchTime.value == 0L) {
            _checkPoints.clear()
        }

        StopwatchObj.start()
    }
    fun onPauseStopwatch() { StopwatchObj.pause() }
    fun onStopStopwatch() { StopwatchObj.stop() }

    fun onSetTimerTime(time: Long) { TimerObj.setTime(time) }
    fun onStartTimer(context: Context) { TimerObj.start(context) }
    fun onPauseTimer() { TimerObj.pause() }
    fun onStopTimer() { TimerObj.stop() }

    fun onAddCheckPoint() { _checkPoints.add(CheckPoint(currentStopwatchTime.value, true)) }
    fun onRemoveCheckPoint(point: CheckPoint) {
        point.isVisible = false
    }
}