package com.aniruddha81.mediprompt.di

import android.content.Context
import androidx.room.Room
import com.aniruddha81.mediprompt.alarm.AlarmScheduler
import com.aniruddha81.mediprompt.alarm.AlarmSchedulerImpl
import com.aniruddha81.mediprompt.data.local.AlarmDao
import com.aniruddha81.mediprompt.data.local.AlarmDatabase
import com.aniruddha81.mediprompt.data.repository.AlarmRepoImpl
import com.aniruddha81.mediprompt.data.repository.AlarmRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlarmDiModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): AlarmDatabase {
        return Room.databaseBuilder(
            context,
            AlarmDatabase::class.java,
            "alarmDB"
        ).build()
    }

    @Provides
    @Singleton
    fun providesAlarmDao(database: AlarmDatabase): AlarmDao = database.getAlarmDao()

    @Provides
    @Singleton
    fun providesAlarmRepository(alarmDao: AlarmDao): AlarmRepository {
        return AlarmRepoImpl(alarmDao)
    }

    @Provides
    @Singleton
    fun providesAlarmScheduler(@ApplicationContext context: Context): AlarmScheduler {
        return AlarmSchedulerImpl(context)
    }
}