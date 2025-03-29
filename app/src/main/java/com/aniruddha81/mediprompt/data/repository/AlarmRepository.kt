package com.aniruddha81.mediprompt.data.repository

import com.aniruddha81.mediprompt.models.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    suspend fun insertAlarm(alarm: Alarm)
    fun getAllAlarm(): Flow<List<Alarm>>
    suspend fun upsertAlarm(alarm: Alarm)
    suspend fun deleteAlarm(alarm: Alarm)
    suspend fun getAlarmById(alarmId: Long): Alarm
    suspend fun deleteAlarmById(alarmId: Long)
}