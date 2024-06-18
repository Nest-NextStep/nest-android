package com.bangkit.nest.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bangkit.nest.data.Result
import com.bangkit.nest.data.remote.request.TaskRequest
import com.bangkit.nest.data.remote.response.StatusResponse
import com.bangkit.nest.data.remote.response.Task
import com.bangkit.nest.data.remote.response.TaskResponse
import com.bangkit.nest.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class TaskRepository private constructor(
    private val apiService: ApiService,
    private val userPrefRepository: UserPrefRepository
) {

    private val _tasks = MutableLiveData<Result<TaskResponse>>()
    val tasks: LiveData<Result<TaskResponse>> = _tasks

    private val _completedTasks = MutableLiveData<Result<List<Task>>>()
    val completedTasks: LiveData<Result<List<Task>>> = _completedTasks

    private val _createTaskResult = MutableLiveData<Result<StatusResponse>>()
    val createTaskResult: LiveData<Result<StatusResponse>> = _createTaskResult

    private val _setTaskCompletedResult = MutableLiveData<Result<StatusResponse>>()
    val setTaskCompletedResult: LiveData<Result<StatusResponse>> = _setTaskCompletedResult

    private val _taskById = MutableLiveData<Result<Task>>()
    val taskById: LiveData<Result<Task>> = _taskById

    private val _updateTaskResult = MutableLiveData<Result<StatusResponse>>()
    val updateTaskResult: LiveData<Result<StatusResponse>> = _updateTaskResult

    private val _deleteTaskResult = MutableLiveData<Result<StatusResponse>>()
    val deleteTaskResult: LiveData<Result<StatusResponse>> = _deleteTaskResult

    suspend fun fetchUserTasks() {
        _tasks.value = Result.Loading
        try {
            val userModel = runBlocking { userPrefRepository.getSession().first() }
            val username = userModel.username
            val response = apiService.getUserTasks(username)
            _tasks.postValue(Result.Success(response))
        } catch (e: Exception) {
            Log.d(TAG, "Failed to get user tasks: ${e.message}")
            _tasks.postValue(Result.Error(e.message.toString()))
        }
    }

    suspend fun fetchUserCompletedTasks() {
        _completedTasks.value = Result.Loading
        try {
            val userModel = runBlocking { userPrefRepository.getSession().first() }
            val username = userModel.username
            val response = apiService.getUserCompletedTasks(username)
            _completedTasks.postValue(Result.Success(response))
        } catch (e: Exception) {
            Log.d(TAG, "Failed to get user completed tasks: ${e.message}")
            _completedTasks.postValue(Result.Error(e.message.toString()))
        }
    }

    suspend fun submitNewTask(taskRequest: TaskRequest) {
        _createTaskResult.value = Result.Loading
        try {
            val userModel = runBlocking { userPrefRepository.getSession().first() }
            val username = userModel.username
            val response = apiService.submitNewTask(username, taskRequest)
            _createTaskResult.postValue(Result.Success(response))
        } catch (e: Exception) {
            Log.d(TAG, "Failed to submit new task: ${e.message}")
            _createTaskResult.postValue(Result.Error(e.message.toString()))
        }
    }

    suspend fun fetchDetailTaskById(taskId: String) {
        _taskById.value = Result.Loading
        try {
            val response = apiService.getDetailTaskById(taskId)
            if (response.isNotEmpty()) {
                _taskById.postValue(Result.Success(response[0]))
            } else {
                _taskById.postValue(Result.Error("Task not found"))
            }
        } catch (e: Exception) {
            Log.d(TAG, "Failed to get task by ID: ${e.message}")
            _taskById.postValue(Result.Error(e.message.toString()))
        }
    }

    suspend fun setTaskCompleted(taskId: Int) {
        _setTaskCompletedResult.value = Result.Loading
        try {
            val response = apiService.setTaskCompleted(taskId)
            _setTaskCompletedResult.postValue(Result.Success(response))
        } catch (e: Exception) {
            Log.d(TAG, "Failed to set task as completed: ${e.message}")
            _setTaskCompletedResult.postValue(Result.Error(e.message.toString()))
        }
    }

    suspend fun updateTask(taskId: Int, taskRequest: TaskRequest) {
        _updateTaskResult.value = Result.Loading
        try {
            val response = apiService.updateTask(taskId, taskRequest)
            _updateTaskResult.postValue(Result.Success(response))
        } catch (e: Exception) {
            Log.d(TAG, "Failed to update task: ${e.message}")
            _updateTaskResult.postValue(Result.Error(e.message.toString()))
        }
    }

    suspend fun deleteTask(taskId: Int) {
        _deleteTaskResult.value = Result.Loading
        try {
            val response = apiService.deleteTask(taskId)
            _deleteTaskResult.postValue(Result.Success(response))
        } catch (e: Exception) {
            Log.d(TAG, "Failed to delete task: ${e.message}")
            _deleteTaskResult.postValue(Result.Error(e.message.toString()))
        }
    }
    companion object {
        private const val TAG = "TaskRepository"

        @Volatile
        private var instance: TaskRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPrefRepository: UserPrefRepository
        ): TaskRepository =
            instance ?: synchronized(this) {
                instance ?: TaskRepository(apiService, userPrefRepository)
            }.also { instance = it }
    }
}
