package com.aniruddha81.mediprompt

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.aniruddha81.mediprompt.Constants.ALARM_CHANNEL_NAME
import dagger.hilt.android.HiltAndroidApp

/*
*   this class is used to create a notification channel for the alarm
*/

@HiltAndroidApp
class AlarmApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val name = getString(R.string.reminder)
        val channelDescription = getString(R.string.reminder_channel_desc)

        val channel = NotificationChannel(
            ALARM_CHANNEL_NAME,
            name,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = channelDescription
        }

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}