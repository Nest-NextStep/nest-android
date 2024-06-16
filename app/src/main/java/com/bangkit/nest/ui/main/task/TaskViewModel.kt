package com.bangkit.nest.ui.main.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.nest.data.Result
import com.bangkit.nest.data.remote.request.TaskRequest
import com.bangkit.nest.data.remote.response.StatusResponse
import com.bangkit.nest.data.remote.response.Task
import com.bangkit.nest.data.remote.response.TaskResponse
import com.bangkit.nest.data.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    val tasks: LiveData<Result<TaskResponse>> = taskRepository.tasks
    val completedTasks: LiveData<Result<List<Task>>> = taskRepository.completedTasks
    val setTaskCompletedResult: LiveData<Result<StatusResponse>> = taskRepository.setTaskCompletedResult
    val taskDetail: LiveData<Result<Task>> = taskRepository.taskById
    val createTaskResult: LiveData<Result<StatusResponse>> = taskRepository.createTaskResult
    val updateTaskResult: LiveData<Result<StatusResponse>> = taskRepository.updateTaskResult
    val deleteTaskResult: LiveData<Result<StatusResponse>> = taskRepository.deleteTaskResult

    fun fetchUserTasks() {
        viewModelScope.launch {
            taskRepository.fetchUserTasks()
        }
    }
    fun fetchUserCompletedTasks() {
        viewModelScope.launch {
            taskRepository.fetchUserCompletedTasks()
        }
    }

    fun fetchDetailTaskById(taskId: String) {
        viewModelScope.launch {
            taskRepository.fetchDetailTaskById(taskId)
        }
    }

    fun setTaskCompleted(taskId: Int) {
        viewModelScope.launch {
            taskRepository.setTaskCompleted(taskId)
        }
    }

    fun clearSetTaskCompletedResult() {
        (setTaskCompletedResult as MutableLiveData).value = null
    }

    fun submitNewTask(taskRequest: TaskRequest) {
        viewModelScope.launch {
            taskRepository.submitNewTask(taskRequest)
        }
    }

    fun clearCreateTaskResult() {
        (createTaskResult as MutableLiveData).value = null
    }

    fun updateTask(taskId: Int, taskRequest: TaskRequest) {
        viewModelScope.launch {
            taskRepository.updateTask(taskId, taskRequest)
        }
    }

    fun clearUpdateTaskResult() {
        (updateTaskResult as MutableLiveData).value = null
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            taskRepository.deleteTask(taskId)
        }
    }

    fun clearDeleteTaskResult() {
        (deleteTaskResult as MutableLiveData).value = null
    }
}
