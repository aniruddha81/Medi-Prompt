package com.aniruddha81.mediprompt.alarm

import com.aniruddha81.mediprompt.models.Alarm


interface AlarmScheduler {
    fun schedule(alarm: Alarm)
    fun cancel(alarm: Alarm)
}