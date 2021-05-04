package ru.awawa.clockutils.helper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.*
import kotlin.concurrent.timer

object Stopwatch {

    private const val updateInterval = 23L

    private val mTime = MutableLiveData(0L)
    val time: LiveData<Long> = mTime

    private var timer: Timer? = null
    private var mIsRunning = MutableLiveData(false)
    val isRunning: LiveData<Boolean> = mIsRunning

    fun start() {
        timer = timer(initialDelay = 0L, period = updateInterval) {
            mTime.postValue(mTime.value!! + updateInterval)
        }
        mIsRunning.postValue(true)
    }

    fun pause() {
        timer?.cancel()
        timer?.purge()
        timer = null
        mIsRunning.postValue(false)
    }

    fun stop() {
        timer?.cancel()
        timer?.purge()
        timer = null
        mTime.postValue(0L)
        mIsRunning.postValue(false)
    }
}