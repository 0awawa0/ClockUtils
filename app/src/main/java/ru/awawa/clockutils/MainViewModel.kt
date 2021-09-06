package ru.awawa.clockutils

import android.content.Context
import androidx.lifecycle.ViewModel
import ru.awawa.clockutils.helper.StopwatchObj
import ru.awawa.clockutils.helper.TimerObj

class MainViewModel: ViewModel() {

    val currentStopwatchTime = StopwatchObj.time
    val isStopwatchRunning = StopwatchObj.isRunning

    val checkPoints = LinkedHashSet<Long>()

    val currentTimerTime = TimerObj.time
    val isTimerRunning = TimerObj.isRunning

    fun onStartStopwatch() {
        if (currentStopwatchTime.value == 0L) {
            checkPoints.clear()
        }

        StopwatchObj.start()
    }
    fun onPauseStopwatch() { StopwatchObj.pause() }
    fun onStopStopwatch() { StopwatchObj.stop() }

    fun onSetTimerTime(time: Long) { TimerObj.setTime(time) }
    fun onStartTimer(context: Context) { TimerObj.start(context) }
    fun onPauseTimer() { TimerObj.pause() }
    fun onStopTimer() { TimerObj.stop() }

    fun onAddCheckPoint() { checkPoints.add(currentStopwatchTime.value) }
    fun onRemoveCheckPoint(value: Long) { checkPoints.remove(value) }
}