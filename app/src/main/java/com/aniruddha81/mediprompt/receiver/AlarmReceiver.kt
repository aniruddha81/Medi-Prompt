package com.aniruddha81.mediprompt.receiver

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.aniruddha81.mediprompt.Constants.ALARM_CHANNEL_NAME
import com.aniruddha81.mediprompt.Constants.ALARM_ID
import com.aniruddha81.mediprompt.Constants.MESSAGE
import com.aniruddha81.mediprompt.Constants.STOP_ALARM
import com.aniruddha81.mediprompt.Constants.TITLE
import com.aniruddha81.mediprompt.MainActivity
import com.aniruddha81.mediprompt.R
import com.aniruddha81.mediprompt.services.AlarmDeleteService

class AlarmReceiver : BroadcastReceiver() {

    // Static companion object to hold the active MediaPlayer
    companion object {
        private var activeMediaPlayer: MediaPlayer? = null

        fun stopAlarmSound() {
            try {
                activeMediaPlayer?.apply {
                    if (isPlaying) {
                        stop()
                    }
                    release()
                }
                activeMediaPlayer = null
                Log.d("AlarmReceiver", "Alarm sound stopped")
            } catch (e: Exception) {
                Log.e("AlarmReceiver", "Error stopping sound: ${e.message}")
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Handle the stop action immediately without creating a new MediaPlayer
        if (intent?.action == STOP_ALARM) {
            val alarmId = intent.getLongExtra(ALARM_ID, -1L)
            Log.d("AlarmReceiver", "Stop button clicked for alarm ID: $alarmId")

            // Cancel the notification
            NotificationManagerCompat.from(context).cancel(alarmId.toInt())

            // Stop the alarm sound using our companion method
            stopAlarmSound()

            // Cancel the alarm in AlarmManager
            val cancelIntent = Intent(context, AlarmReceiver::class.java)
            val pIntent = PendingIntent.getBroadcast(
                context,
                alarmId.toInt(),
                cancelIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pIntent)

            // Delete the alarm from database via service
            val deleteServiceIntent = Intent(context, AlarmDeleteService::class.java).apply {
                putExtra(ALARM_ID, alarmId)
                // Add flags to keep app open
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startService(deleteServiceIntent)

            return
        }

        // This is a new alarm, not a stop action
        val title = intent?.getStringExtra(TITLE) ?: return
        val message = intent.getStringExtra(MESSAGE)
        val alarmId = intent.getLongExtra(ALARM_ID, 1L)

        // Create and start a new MediaPlayer for this alarm
        try {
            // First stop any existing alarm sound
            stopAlarmSound()

            // Create a new MediaPlayer
            activeMediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI).apply {
                isLooping = true
                start()
            }
            Log.d("AlarmReceiver", "Started alarm sound for ID: $alarmId")
        } catch (e: Exception) {
            Log.e("AlarmReceiver", "Error playing sound: ${e.message}")
        }

        // Create the notification with action
        val goIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        val goPendingIntent = PendingIntent.getActivity(context, 0, goIntent, PendingIntent.FLAG_IMMUTABLE)

        val stopIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = STOP_ALARM
            putExtra(ALARM_ID, alarmId)
        }

        val stopPendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId.toInt(),
            stopIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, ALARM_CHANNEL_NAME)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(goPendingIntent)
            .addAction(
                R.drawable.ic_launcher_foreground,
                "Stop",
                stopPendingIntent
            )
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)  // Don't auto-cancel to ensure user sees the notification
            .build()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                NotificationManagerCompat.from(context).notify(alarmId.toInt(), notification)
            }
        } else {
            NotificationManagerCompat.from(context).notify(alarmId.toInt(), notification)
        }
    }
}