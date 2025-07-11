package com.aniruddha81.mediprompt.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.aniruddha81.mediprompt.Constants.ALARM_ID
import com.aniruddha81.mediprompt.data.repository.AlarmRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Service responsible for deleting alarms from the database.
 * Used when an alarm is dismissed from the notification.
 */
@AndroidEntryPoint
class AlarmDeleteService : Service() {

    @Inject
    lateinit var alarmRepository: AlarmRepository

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val alarmId = it.getLongExtra(ALARM_ID, -1L)
            if (alarmId != -1L) {
                deleteAlarm(alarmId)
            }
        }
        return START_NOT_STICKY
    }

    private fun deleteAlarm(alarmId: Long) {
        serviceScope.launch {
            try {
                Log.d("AlarmDeleteService", "Deleting alarm with ID: $alarmId")

                // Delete the alarm from the repository
                alarmRepository.deleteAlarmById(alarmId)

                // Instead of broadcasting, directly notify the ViewModel
                // The ViewModel observes the Flow from repository, so it will
                // automatically update when data changes

                Log.d("AlarmDeleteService", "Alarm deleted successfully")
            } catch (e: Exception) {
                Log.e("AlarmDeleteService", "Error deleting alarm: ${e.message}")
            } finally {
                // Stop the service after deletion attempt
                stopSelf()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel all coroutines when the service is destroyed
        serviceScope.cancel()
    }
}
