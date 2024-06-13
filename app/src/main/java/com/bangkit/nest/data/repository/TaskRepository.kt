package com.bangkit.nest.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.nest.data.Result
import com.bangkit.nest.data.remote.response.TaskResponse
import com.bangkit.nest.data.remote.retrofit.ApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class TaskRepository private constructor(
    private val apiService: ApiService,
    private val userPrefRepository: UserPrefRepository
) {
    fun getUserTasks(): LiveData<Result<TaskResponse>> = liveData {
        emit(Result.Loading)
        try {
            val userModel = runBlocking { userPrefRepository.getSession().first() }
            val username = userModel.username

            val response = retryIO {
                apiService.getUserTasks(username)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get user tasks: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    private suspend fun <T> retryIO(
        times: Int = 3,
        initialDelay: Long = 1000,
        maxDelay: Long = 10000,
        factor: Double = 2.0,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelay
        repeat(times - 1) {
            try {
                return block()
            } catch (e: Exception) {
                if (e.message?.contains("429") == true) {
                    Log.e(TAG, "Rate limit hit, retrying in $currentDelay ms...")
                    delay(currentDelay)
                    currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
                } else {
                    throw e
                }
            }
        }
        return block() // last attempt
    }

    companion object {
        private const val TAG = "TaskRepository"

        @Volatile
        private var instance: TaskRepository? = null
        fun getInstance(apiService: ApiService, userPrefRepository: UserPrefRepository): TaskRepository =
            instance ?: synchronized(this) {
                instance ?: TaskRepository(apiService, userPrefRepository)
            }.also { instance = it }
    }
}
