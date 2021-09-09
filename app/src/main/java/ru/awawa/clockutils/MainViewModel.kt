package ru.awawa.clockutils

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ru.awawa.clockutils.helper.StopwatchObj
import ru.awawa.clockutils.helper.TimerObj

class MainViewModel: ViewModel() {

    val currentStopwatchTime = StopwatchObj.time
    val isStopwatchRunning = StopwatchObj.isRunning

    data class CheckPoint(val timestamp: Long, val difference: Long)

    private val _checkPoints = LinkedHashSet<CheckPoint>()
    val checkPoints = mutableStateOf(_checkPoints)
    val currentTimerTime = TimerObj.time
    val isTimerRunning = TimerObj.isRunning

    fun onSwitchStopwatch() { StopwatchObj.switch() }
    fun onResetStopwatch() {
        _checkPoints.clear()
        StopwatchObj.reset()
    }

    fun onSetTimerTime(time: Long) { TimerObj.setTime(time) }
    fun onStartTimer(context: Context) { TimerObj.start(context) }
    fun onPauseTimer() { TimerObj.pause() }
    fun onStopTimer() { TimerObj.stop() }

    fun onAddCheckPoint() {
        val time = currentStopwatchTime.value
        val last = if (_checkPoints.isNotEmpty()) _checkPoints.last().timestamp else -1L
        _checkPoints.add(CheckPoint(time, if (last == -1L) -1L else time - last))
    }
}