package com.aniruddha81.mediprompt.data.repository

import com.aniruddha81.mediprompt.data.local.AlarmDao
import com.aniruddha81.mediprompt.models.Alarm
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlarmRepoImpl @Inject constructor(private val alarmDao: AlarmDao) : AlarmRepository {
    override suspend fun insertAlarm(alarm: Alarm) {
        alarmDao.insertAlarm(alarm)
    }

    override fun getAllAlarm(): Flow<List<Alarm>> {
        return alarmDao.getAllAlarm()
    }

    override suspend fun upsertAlarm(alarm: Alarm) {
        alarmDao.upsertAlarm(alarm)
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
        alarmDao.deleteAlarm(alarm)
    }

    override suspend fun getAlarmById(alarmId: Long): Alarm {
        return alarmDao.getAlarmById(alarmId)
    }

    override suspend fun deleteAlarmById(alarmId: Long) {
        alarmDao.deleteAlarmById(alarmId)
    }
}