package com.aniruddha81.mediprompt.viewModels

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aniruddha81.mediprompt.Constants.ALARM_DELETED_ACTION
import com.aniruddha81.mediprompt.alarm.AlarmScheduler
import com.aniruddha81.mediprompt.data.repository.AlarmRepository
import com.aniruddha81.mediprompt.models.Alarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmActivityViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmScheduler: AlarmScheduler,
    application: Application  // Add Application context to register broadcast receiver
) : AndroidViewModel(application) {

    var showAlertDialog = mutableStateOf(false)

    private val _alarms = MutableStateFlow<List<Alarm>>(emptyList())
    val alarms = _alarms.asStateFlow()

    // Broadcast receiver to update the UI when alarms are deleted from notifications
    private val alarmDeletedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ALARM_DELETED_ACTION) {
                Log.d("AlarmViewModel", "Received alarm deleted broadcast")
                // Refresh the alarm list
                getAllAlarm()
            }
        }
    }

    init {
        // Register the broadcast receiver
        ContextCompat.registerReceiver(
            getApplication<Application>(),
            alarmDeletedReceiver,
            IntentFilter(ALARM_DELETED_ACTION),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        // Load all alarms
        getAllAlarm()
    }

    override fun onCleared() {
        super.onCleared()
        // Unregister the receiver when ViewModel is cleared
        try {
            getApplication<Application>().unregisterReceiver(alarmDeletedReceiver)
        } catch (e: Exception) {
            Log.e("AlarmViewModel", "Error unregistering receiver: ${e.message}")
        }
    }

    fun getAllAlarm() {
        viewModelScope.launch {
            alarmRepository.getAllAlarm().collect { alarmsList ->
                _alarms.value = alarmsList
            }
        }
    }

    fun insertAlarm(alarm: Alarm) {
        viewModelScope.launch {
            alarmRepository.insertAlarm(alarm)
        }
    }

    fun deleteAlarmItemById(alarmId: Long) {
        viewModelScope.launch {
            try {
                // Get the alarm details before deleting from database
                val alarmToDelete = alarmRepository.getAlarmById(alarmId)

                // Cancel the alarm with the system's AlarmManager
                alarmScheduler.cancel(alarmToDelete)

                // Then delete from database
                alarmRepository.deleteAlarmById(alarmId)
            } catch (e: Exception) {
                Log.e("AlarmViewModel", "Error deleting alarm: ${e.message}")
                // Handle the case where the alarm might not exist
                // Just delete from database as a fallback
                alarmRepository.deleteAlarmById(alarmId)
            }
        }
    }
}