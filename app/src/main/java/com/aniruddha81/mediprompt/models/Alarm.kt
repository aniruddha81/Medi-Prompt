package com.aniruddha81.mediprompt.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String = "",
    val message: String = "",
    val scheduleAt: Long = 0
)