package com.bangkit.nest.ui.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddTaskViewModel : ViewModel() {

    private val _date = MutableLiveData<Long>()
    val date: LiveData<Long> = _date

    private val _startTime = MutableLiveData<Long>()
    val startTime: LiveData<Long> = _startTime

    private val _endTime = MutableLiveData<Long>()
    val endTime: LiveData<Long> = _endTime

    private val _duration = MutableLiveData<Int>()
    val duration: LiveData<Int> = _duration

    fun setDate(date: Long) {
        _date.value = date
    }

    fun setStartTime(startTime: Long) {
        _startTime.value = startTime
    }

    fun setEndTime(endTime: Long) {
        _endTime.value = endTime
    }

    fun setDuration(duration: Int) {
        _duration.value = duration
    }

    fun addTask(task: Task) {
        // Logic to save task to the database
    }
}
