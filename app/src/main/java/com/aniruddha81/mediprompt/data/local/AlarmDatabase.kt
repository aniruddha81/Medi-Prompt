package com.aniruddha81.mediprompt.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aniruddha81.mediprompt.models.Alarm

@Database(entities = [Alarm::class], version = 1, exportSchema = false)
abstract class AlarmDatabase() : RoomDatabase() {
    abstract fun getAlarmDao(): AlarmDao
}