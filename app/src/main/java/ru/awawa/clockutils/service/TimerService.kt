package ru.awawa.clockutils.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import ru.awawa.clockutils.MainActivity
import ru.awawa.clockutils.R
import ru.awawa.clockutils.helper.TimerObj
import java.util.*
import kotlin.random.Random.Default.nextInt

class TimerService: LifecycleService() {

    companion object {
        private const val ACTION_START = "ru.awawa.clockutils.timer.ACTION_START"
        private const val ACTION_PAUSE = "ru.awawa.clockutils.timer.ACTION_PAUSE"
        private const val ACTION_STOP = "ru.awawa.clockutils.timer.ACTION_STOP"
        private const val CHANNEL_ID = "ru.awawa.clockuitls.timer_service"
        private const val SERVICE_ID = 102
    }

    private val channelName = getString(R.string.timer_notification_channel_name)
    private val intentFilter = IntentFilter()
    private val broadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent ?: return

            val action = intent.action ?: return

            val time = TimerObj.time.value
            val seconds = "%02d".format(time / 1000 % 60)
            val minutes = "%02d".format(time / 1000 / 60 % 60)
            val hours = "%02d".format(time / 1000 / 60 / 60)

            when (action) {
                ACTION_START -> {
                    TimerObj.start()
                    updateNotification(
                        "$hours:$minutes:$seconds",
                        isRunning = true,
                        force = true
                    )
                }
                ACTION_PAUSE -> {
                    TimerObj.pause()
                    updateNotification(
                        "$hours:$minutes:$seconds",
                        isRunning = false,
                        force = true
                    )
                }
                ACTION_STOP -> {
                    stopSelf()
                    TimerObj.stop()
                }
                else -> return
            }
        }
    }

    private var lastUpdate = 0L

    init {
        intentFilter.addAction(ACTION_START)
        intentFilter.addAction(ACTION_PAUSE)
        intentFilter.addAction(ACTION_STOP)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        lifecycleScope.launchWhenStarted {
            TimerObj.time.collect {
                val seconds = "%02d".format(it / 1000 % 60)
                val minutes = "%02d".format(it / 1000 / 60 % 60)
                val hours = "%02d".format(it / 1000 / 60 / 60)
                updateNotification(
                    "$hours:$minutes:$seconds",
                    TimerObj.isRunning.value
                )
            }
        }
        startForeground(SERVICE_ID, buildNotification("00:00:00", true))
        registerReceiver(broadcastReceiver, intentFilter)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
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
            R.drawable.ic_timer,
            getString(R.string.start),
            Intent(ACTION_START).let {
                PendingIntent.getBroadcast(this, startRequestCode, it, 0)
            }
        )

        val actionPause = NotificationCompat.Action(
            R.drawable.ic_timer,
            getString(R.string.pause),
            Intent(ACTION_PAUSE).let {
                PendingIntent.getBroadcast(this, pauseRequestCode, it, 0)
            }
        )

        val actionStop = NotificationCompat.Action(
            R.drawable.ic_timer,
            getString(R.string.stop),
            Intent(ACTION_STOP).let {
                PendingIntent.getBroadcast(this, stopRequestCode, it, 0)
            }
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_timer)
            .setContentTitle(getString(R.string.timer))
            .setContentText(time)
            .setOngoing(true)
            .setSound(null)
            .setContentIntent(Intent(this, MainActivity::class.java).let {
                it.putExtra(MainActivity.EXTRA_TYPE, MainActivity.TYPE_TIMER)
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