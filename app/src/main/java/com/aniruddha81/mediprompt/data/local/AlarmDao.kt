package com.aniruddha81.mediprompt.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.aniruddha81.mediprompt.models.Alarm
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: Alarm)

    @Query("SELECT * FROM Alarm ORDER BY id DESC")
    fun getAllAlarm(): Flow<List<Alarm>>

    @Upsert
    suspend fun upsertAlarm(alarm: Alarm)

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)

    @Query("SELECT * FROM Alarm WHERE id = :alarmId ")
    suspend fun getAlarmById(alarmId: Long): Alarm

    @Query("DELETE FROM Alarm WHERE id = :alarmId")
    suspend fun deleteAlarmById(alarmId: Long)
}