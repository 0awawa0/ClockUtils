package ru.awawa.clockutils.helper

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import ru.awawa.clockutils.MainActivity
import ru.awawa.clockutils.R
import java.util.*
import kotlin.random.Random.Default.nextInt

class StopwatchSerivce: LifecycleService() {

    companion object {
        const val ACTION_START = "ru.awawa.clockutils.stopwatch.ACTION_START"
        const val ACTION_PAUSE = "ru.awawa.clockutils.stopwatch.ACTION_PAUSE"
        const val ACTION_STOP = "ru.awawa.clockutils.stopwatch.ACTION_STOP"
        const val CHANNEL_ID = "ru.awawa.clockutils.stopwatch_service"
        const val CHANNEL_NAME = "Stopwatch/timer notification"
        const val SERVICE_ID = 101
    }

    private val intentFilter = IntentFilter()
    private val broadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent ?: return
            val action = intent.action ?: return

            val time = Stopwatch.time.value ?: 0L
            val seconds = "%02d".format(time / 1000 % 60)
            val minutes = "%02d".format(time / 1000 / 60 % 60)
            val hours = "%02d".format(time / 1000 / 60 / 60)

            when (action) {
                ACTION_START -> {
                    Stopwatch.start()
                    updateNotification(
                        "$hours:$minutes:$seconds",
                        isRunning = true,
                        force = true
                    )
                }
                ACTION_PAUSE -> {
                    Stopwatch.pause()
                    updateNotification(
                        "$hours:$minutes:$seconds",
                        isRunning = false,
                        force = true
                    )
                }
                ACTION_STOP -> {
                    Stopwatch.time.removeObserver(observer)
                    Stopwatch.stop()
                    stopService(Intent(
                        this@StopwatchSerivce,
                        StopwatchSerivce::class.java
                    ))
                }
                else -> return
            }
        }
    }

    private val observer = Observer<Long> {
        val seconds = "%02d".format(it / 1000 % 60)
        val minutes = "%02d".format(it / 1000 / 60 % 60)
        val hours = "%02d".format(it / 1000 / 60 / 60)
        updateNotification(
            "$hours:$minutes:$seconds",
            Stopwatch.isRunning.value ?: false
        )
    }

    private var lastUpdate = 0L

    init {
        intentFilter.addAction(ACTION_START)
        intentFilter.addAction(ACTION_PAUSE)
        intentFilter.addAction(ACTION_STOP)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Stopwatch.time.observe(this, observer)
        startForeground(SERVICE_ID, buildNotification("00:00:00", true))
        registerReceiver(broadcastReceiver, intentFilter)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Stopwatch.time.removeObserver(observer)
        unregisterReceiver(broadcastReceiver)
        stopForeground(true)
        super.onDestroy()
    }

    private fun updateNotification(time: String, isRunning: Boolean, force: Boolean = false) {
        val currTime = Date().time
        if (currTime - lastUpdate < 200 && !force) return
        lastUpdate = currTime
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager?
        manager?.notify(SERVICE_ID, buildNotification(time, isRunning))
    }

    private fun buildNotification(time: String, isRunning: Boolean): Notification {

        val startRequestCode = nextInt()
        val pauseRequestCode = nextInt()
        val stopRequestCode = nextInt()

        Log.d("ForegroundService", "Building notification with isRunning: $isRunning")
        createNotificationChannel()
        val actionStart = NotificationCompat.Action(
            R.drawable.ic_stopwatch,
            "Start",
            Intent(ACTION_START).let {
                PendingIntent.getBroadcast(this, startRequestCode, it, 0)
            }
        )

        val actionPause = NotificationCompat.Action(
            R.drawable.ic_stopwatch,
            "Pause",
            Intent(ACTION_PAUSE).let {
                PendingIntent.getBroadcast(this, pauseRequestCode, it, 0)
            }
        )

        val actionStop = NotificationCompat.Action(
            R.drawable.ic_stopwatch,
            "Stop",
            Intent(ACTION_STOP).let {
                PendingIntent.getBroadcast(this, stopRequestCode, it, 0)
            }
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Stopwatch")
            .setContentText(time)
            .setOngoing(true)
            .setSound(null)
            .setContentIntent(Intent(this, MainActivity::class.java).let {
                PendingIntent.getActivity(
                    this,
                    SERVICE_ID,
                    it,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            })

        if (isRunning) builder.addAction(actionPause)
        else builder.addAction(actionStart)

        builder.addAction(actionStop)

        return builder.build()

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                    as? NotificationManager? ?: return
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.setSound(null, null)
            notificationManager.createNotificationChannel(channel)
        }
    }
}