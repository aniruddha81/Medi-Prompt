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

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI)
        mediaPlayer.isLooping = true

        if (intent?.action == STOP_ALARM) {
            val alarmId = intent.getIntExtra(ALARM_ID, 2)
            NotificationManagerCompat.from(context).cancel(alarmId)

            mediaPlayer.release()
            mediaPlayer.stop()

//            val alarmId = intent.getIntExtra(ALARM_ID, 2)
//            NotificationManagerCompat.from(context).cancel(alarmId)

            val intent = Intent(context, AlarmReceiver::class.java)
            val pIntent =
                PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_IMMUTABLE)
            alarmManager.cancel(pIntent)

            return
        }

        val title = intent?.getStringExtra(TITLE) ?: return
        val message = intent.getStringExtra(MESSAGE)
        val alarmId = intent.getIntExtra(ALARM_ID, 1)

        val goIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }  // this intent will be used to open the app when the notification is clicked

        val goPendingIntent =
            PendingIntent.getActivity(context, 1, goIntent, PendingIntent.FLAG_IMMUTABLE)

//        now i have to write the code of the button of cancelling the alarm and the notification

        val stopIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = STOP_ALARM
            putExtra(ALARM_ID, alarmId)
        }     // this intent will be used to stop the alarm

        val stopPendingIntent = PendingIntent.getBroadcast(
            context,
            2,
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
            .build()

        mediaPlayer.start()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                NotificationManagerCompat.from(context).notify(1, notification)
            }
        } else {
            NotificationManagerCompat.from(context).notify(1, notification)
        }
    }
}