package com.bangkit.nest.data.remote.response

import com.google.gson.annotations.SerializedName
import java.time.LocalTime
import java.util.Date

data class TaskResponse(

    @field:SerializedName("completedCount")
    val completedCount: Int? = null,

    @field:SerializedName("totalCount")
    val totalCount: Int? = null,

    @field:SerializedName("tasks")
    val tasks: List<Task?>? = null
)

data class Task(

    @field:SerializedName("task_id")
    val id: Int,

    @field:SerializedName("task_name")
    val title: String,

    @field:SerializedName("task_date")
    val date: String,

    @field:SerializedName("task_priority")
    val priority: String,

    @field:SerializedName("task_focusTime")
    val focusTime: Int,

    @field:SerializedName("task_duration")
    val duration: Int,

    @field:SerializedName("task_repeat")
    val isRepeated: Int,

    @field:SerializedName("task_startTime")
    val startTime: String,

    @field:SerializedName("task_breakTime")
    val breakTime: Int,

    @field:SerializedName("task_endTime")
    val endTime: String,

    @field:SerializedName("isCompleted")
    val isCompleted: Int
)

data class StatusResponse(

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("message")
    val message: String? = null,
)