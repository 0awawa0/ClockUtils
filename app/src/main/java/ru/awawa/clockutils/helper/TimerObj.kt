package ru.awawa.clockutils.helper

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import kotlin.concurrent.timer

object TimerObj {

    private const val updateInterval = 23L

    private var timer: Timer? = null

    private val mTime = MutableStateFlow(0L)
    val time: StateFlow<Long> = mTime

    private var mIsRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = mIsRunning

    fun setTime(time: Long) { mTime.value = time }

    fun start() {
        timer = timer(initialDelay = updateInterval, period = updateInterval) {
            val newVal = mTime.value - updateInterval
            mTime.value = if (newVal > 0) newVal else 0
        }
    }

    fun pause() {
        timer?.cancel()
        timer?.purge()
        timer = null
        mIsRunning.value = false
    }

    fun stop() {
        timer?.cancel()
        timer?.purge()
        timer = null
        mTime.value = 0L
        mIsRunning.value = false
    }
}