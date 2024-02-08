package com.example.productivitytimer.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.annotation.RequiresApi
import com.example.productivitytimer.R
import androidx.core.app.NotificationCompat
import com.example.productivitytimer.MainActivity

class TimerService: Service(){
    private lateinit var runnable: Runnable
    private var elapsedTime = 0
    private var handler = Handler(Looper.getMainLooper())
    private var shouldRun = true

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.action == ACTION_STOP_SERVICE) {
            stopSelf()
            shouldRun = false
            return START_NOT_STICKY
        }

        startForegroundService()
        return START_STICKY
    }


    private fun startForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel()

        val notification = buildNotification()
        startForeground(NOTIFICATION_ID, notification)

        runnable = object : Runnable {
            override fun run() {
                if(!shouldRun) return
                elapsedTime++
                updateNotification(buildNotification())
                handler.postDelayed(this, 1000) // Update every second
            }
        }
        handler.post(runnable)
    }



    private fun buildNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Productivity Timer")
            .setContentText("Time: $elapsedTime")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun updateNotification(updatedNotification: Notification) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, updatedNotification)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            CHANNEL_ID,
            "Foreground Service Channel",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(serviceChannel)
    }

    companion object {
        const val CHANNEL_ID = "ForegroundServiceChannel"
        const val ACTION_STOP_SERVICE = "action_stop_service"
        const val NOTIFICATION_ID = 1
    }
}