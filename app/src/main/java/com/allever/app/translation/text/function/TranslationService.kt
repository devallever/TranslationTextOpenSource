package com.allever.app.translation.text.function

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.allever.app.translation.text.R
import com.allever.app.translation.text.ui.MainDrawerActivity
import com.allever.lib.common.app.App
import com.allever.lib.common.util.log

class TranslationService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("启动服务")
        val paddingFlag = 1
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainDrawerActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT,
            null
        )
        val notificationBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "translation"
            val channelName = "翻译"
            val importance = NotificationManager.IMPORTANCE_HIGH
            createNotificationChannel(channelId, channelName, importance)
            NotificationCompat.Builder(this, channelId)
                .setNumber(paddingFlag)
        } else {
            NotificationCompat.Builder(this)
        }
        notificationBuilder
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.notification_msg, getString(R.string.app_name)))
            .setSmallIcon(R.drawable.ic_logo)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_logo))
            .setContentIntent(pendingIntent)
        startForeground(1, notificationBuilder.build())
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
        log("停止服务")
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String, importance: Int) {
        val channel = NotificationChannel(channelId, channelName, importance)
        val notificationManager = App.context.getSystemService(
            NOTIFICATION_SERVICE
        ) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, TranslationService::class.java)
            context.startService(intent)
        }

        fun stop(context: Context) {
            val intent = Intent(context, TranslationService::class.java)
            context.stopService(intent)
        }
    }
}