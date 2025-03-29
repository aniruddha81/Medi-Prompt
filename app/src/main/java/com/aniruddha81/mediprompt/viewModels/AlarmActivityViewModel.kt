package com.aniruddha81.mediprompt.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aniruddha81.mediprompt.data.repository.AlarmRepository
import com.aniruddha81.mediprompt.models.Alarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel remains largely the same
@HiltViewModel
class AlarmActivityViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository
) : ViewModel() {

    var showAlertDialog = mutableStateOf(false)

    private val _alarms = MutableStateFlow<List<Alarm>>(emptyList())
    val alarms = _alarms.asStateFlow()

    init {
        getAllAlarm()
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
            alarmRepository.deleteAlarmById(alarmId)
        }
    }
}