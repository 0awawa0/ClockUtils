package ru.awawa.clockutils.helper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import kotlin.concurrent.timer

object Stopwatch {

    private const val updateInterval = 23L

    private val mTime = MutableStateFlow(0L)
    val time: StateFlow<Long> = mTime

    private var timer: Timer? = null
    private var mIsRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = mIsRunning

    fun start() {
        timer = timer(initialDelay = 0L, period = updateInterval) {
            mTime.value = mTime.value + updateInterval
        }
        mIsRunning.value = true
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