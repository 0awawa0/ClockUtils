package ru.awawa.clockutils.service

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import ru.awawa.clockutils.BuildConfig
import ru.awawa.clockutils.MainActivity
import ru.awawa.clockutils.R
import ru.awawa.clockutils.helper.StopwatchObj
import java.util.*
import kotlin.random.Random.Default.nextInt

class StopwatchService: LifecycleService() {

    companion object {
        private const val ACTION_START = "${BuildConfig.APPLICATION_ID}.stopwatch.ACTION_START"
        private const val ACTION_PAUSE = "${BuildConfig.APPLICATION_ID}.stopwatch.ACTION_PAUSE"
        private const val ACTION_STOP = "${BuildConfig.APPLICATION_ID}.stopwatch.ACTION_STOP"
        private const val CHANNEL_ID = "${BuildConfig.APPLICATION_ID}.stopwatch_service"
        private const val SERVICE_ID = 101
    }

    private val tag = "StopwatchService"

    private val intentFilter = IntentFilter()
    private val broadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent ?: return
            val action = intent.action ?: return

            val time = StopwatchObj.time.value
            val seconds = "%02d".format(time / 1000 % 60)
            val minutes = "%02d".format(time / 1000 / 60 % 60)
            val hours = "%02d".format(time / 1000 / 60 / 60)

            Log.w(tag, "Broadcast received: $action")
            when (action) {
                ACTION_START -> {
                    StopwatchObj.start()
                    updateNotification(
                        "$hours:$minutes:$seconds",
                        isRunning = true,
                        force = true
                    )
                }
                ACTION_PAUSE -> {
                    StopwatchObj.pause()
                    updateNotification(
                        "$hours:$minutes:$seconds",
                        isRunning = false,
                        force = true
                    )
                }
                ACTION_STOP -> {
                    stopSelf()
                    StopwatchObj.stop()
                }
                else -> return
            }
        }
    }

    private var lastUpdate = 0L
    private var timeUpdaterJob: Job? = null

    init {
        intentFilter.addAction(ACTION_START)
        intentFilter.addAction(ACTION_PAUSE)
        intentFilter.addAction(ACTION_STOP)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.w(tag, "Service started")
        timeUpdaterJob = lifecycleScope.launchWhenStarted {
                StopwatchObj.time.collect {
                    val seconds = "%02d".format(it / 1000 % 60)
                    val minutes = "%02d".format(it / 1000 / 60 % 60)
                    val hours = "%02d".format(it / 1000 / 60 / 60)
                    updateNotification(
                        "$hours:$minutes:$seconds",
                        StopwatchObj.isRunning.value
                    )
                }
        }
        startForeground(SERVICE_ID, buildNotification("00:00:00", true))
        registerReceiver(broadcastReceiver, intentFilter)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        timeUpdaterJob?.cancel()
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

        createNotificationChannel()
        val actionStart = NotificationCompat.Action(
            R.drawable.ic_stopwatch,
            getString(R.string.start),
            Intent(ACTION_START).let {
                PendingIntent.getBroadcast(this, startRequestCode, it, 0)
            }
        )

        val actionPause = NotificationCompat.Action(
            R.drawable.ic_stopwatch,
            getString(R.string.pause),
            Intent(ACTION_PAUSE).let {
                PendingIntent.getBroadcast(this, pauseRequestCode, it, 0)
            }
        )

        val actionStop = NotificationCompat.Action(
            R.drawable.ic_stopwatch,
            getString(R.string.stop),
            Intent(ACTION_STOP).let {
                PendingIntent.getBroadcast(this, stopRequestCode, it, 0)
            }
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stopwatch)
            .setContentTitle(getString(R.string.stopwatch))
            .setContentText(time)
            .setOngoing(isRunning)
            .setSound(null)
            .setContentIntent(Intent(this, MainActivity::class.java).let {
                it.putExtra(MainActivity.EXTRA_TYPE, MainActivity.TYPE_STOPWATCH)
                PendingIntent.getActivity(
                    this,
                    SERVICE_ID,
                    it,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            })

        builder.addAction(if (isRunning) actionPause else actionStart)
        builder.addAction(actionStop)

        return builder.build()

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                    as? NotificationManager? ?: return
            val channelName = getString(R.string.stopwatch_notification_channel_name)
            val channel = NotificationChannel(
                CHANNEL_ID,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.setSound(null, null)
            notificationManager.createNotificationChannel(channel)
        }
    }
}