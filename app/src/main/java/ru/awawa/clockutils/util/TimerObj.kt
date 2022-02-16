package ru.awawa.clockutils.util

import android.content.Context
import android.media.MediaPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.awawa.clockutils.R
import java.util.*
import kotlin.concurrent.timer

object TimerObj {

    private const val updateInterval = 150L

    private var timer: Timer? = null

    private var player: MediaPlayer? = null

    private val mTotalTime = MutableStateFlow(0L)
    val totalTime: StateFlow<Long> = mTotalTime

    private val mTime = MutableStateFlow(0L)
    val time: StateFlow<Long> = mTime

    private var mIsRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = mIsRunning

    fun setTime(time: Long) {
        if (isRunning.value) return
        mTotalTime.value = time
        mTime.value = time
    }

    fun start(context: Context) {
        if (mTime.value == 0L) return

        if (player == null) {
            player = MediaPlayer.create(context, R.raw.timer)
            player?.isLooping = true
            player?.setVolume(1.0f, 1.0f)
        }
        timer = timer(initialDelay = updateInterval, period = updateInterval) {
            val newVal = mTime.value - updateInterval
            mTime.value = if (newVal > 0) newVal else 0
            if (mTime.value == 0L) {
                player?.start()
            }
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
        mTotalTime.value = 0L
        mIsRunning.value = false
        player?.stop()
        player?.release()
        player = null
    }
}