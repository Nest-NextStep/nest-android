package com.bangkit.nest.data.remote.request

data class TaskRequest(

    val taskName: String,
    val taskDate: String,
    val taskStartTime: String,
    val taskEndTime: String,
    val taskDuration: Int,
    val taskFocusTime: Int,
    val taskBreakTime: Int,
    val taskPriority: String,
    val taskRepeat: Int,
    val isCompleted: Int
)
