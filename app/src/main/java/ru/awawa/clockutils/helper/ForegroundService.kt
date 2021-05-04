package ru.awawa.clockutils.helper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import ru.awawa.clockutils.R
import java.util.*

class ForegroundService: LifecycleService() {

    companion object {
        const val ACTION_TIME_UPDATE = "ru.awawa.clockutils.ACTION_TIME_UPDATE"
        const val CHANNEL_ID = "ru.awawa.clockutils.foregroundservice"
        const val CHANNEL_NAME = "Stopwatch/timer notification"
        const val SERVICE_ID = 101
    }

    private val observer = Observer<Long> {
        val seconds = "%02d".format(it / 1000 % 60)
        val minutes = "%02d".format(it / 1000 / 60 % 60)
        val hours = "%02d".format(it / 1000 / 60 / 60)
        updateNotification("$hours:$minutes:$seconds")
    }

    private var lastUpdate = 0L

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Stopwatch.time.observe(this, observer)
        startForeground(SERVICE_ID, buildNotification("00:00:00"))
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Stopwatch.time.removeObserver(observer)
        stopForeground(true)
        super.onDestroy()
    }

    private fun updateNotification(time: String) {
        val currTime = Date().time
        if (currTime - lastUpdate < 200) return
        lastUpdate = currTime
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager?
        manager?.notify(SERVICE_ID, buildNotification(time))
    }

    private fun buildNotification(time: String): Notification {
        createNotificationChannel()
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Stopwatch")
            .setContentText(time)
            .setOngoing(true)
            .setSound(null)
            .build()
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