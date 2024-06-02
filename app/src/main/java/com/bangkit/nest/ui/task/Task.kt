package com.bangkit.nest.ui.task

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime

@Entity(tableName = "tasks")
@RequiresApi(Build.VERSION_CODES.O)
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val date: Long,
    val startTime: LocalTime = LocalTime.now(),
    val endTime: LocalTime = LocalTime.now(),
    val duration: Int,
    val focusTime: Int,
    val breakTime: Int,
    val priority: String,
    val repeat: Boolean
)


