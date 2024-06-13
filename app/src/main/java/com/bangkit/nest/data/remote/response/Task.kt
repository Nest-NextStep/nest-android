package com.bangkit.nest.data.remote.response

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date

data class TaskResponse(
    val tasks: List<Task>,
    val completedCount: Int,
    val totalCount: Int
)
data class Task(
    @SerializedName("task_id")
    val id: Int,

    @SerializedName("task_name")
    val title: String,

    @SerializedName("task_date")
    val date: Date,

    @SerializedName("task_startTime")
    val startTime: LocalTime,

    @SerializedName("task_endTime")
    val endTime: LocalTime,

    @SerializedName("task_duration")
    val duration: Int,

    @SerializedName("task_focusTime")
    val focusTime: Int,

    @SerializedName("task_breakTime")
    val breakTime: Int,

    @SerializedName("task_priority")
    val priority: String,

    @SerializedName("task_repeat")
    val isRepeated: Boolean,

    @SerializedName("isCompleted")
    val isCompleted: Boolean
)



