package ru.awawa.clockutils

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.awawa.clockutils.util.StopwatchObj
import ru.awawa.clockutils.util.TimerObj

class MainViewModel: ViewModel() {

    val currentStopwatchTime = StopwatchObj.time
    val isStopwatchRunning = StopwatchObj.isRunning

    data class CheckPoint(val timestamp: Long, val difference: Long)

    private val _checkPoints = LinkedHashSet<CheckPoint>()
    val checkPoints = mutableStateOf(_checkPoints)

    val currentTimerTime = TimerObj.time
    val totalTimerTime = TimerObj.totalTime
    val isTimerRunning = TimerObj.isRunning

    private var _ticksPerSecond = MutableStateFlow(0f)
    val ticksPerSecond: StateFlow<Float> = _ticksPerSecond

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

    fun onNewTicksPerSecondValue(value: Float) {
        _ticksPerSecond.value = value
    }
}