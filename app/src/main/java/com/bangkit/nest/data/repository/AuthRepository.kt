package com.bangkit.nest.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.nest.data.remote.response.LoginResponse
import com.bangkit.nest.data.remote.retrofit.ApiService
import com.bangkit.nest.data.Result
import com.bangkit.nest.data.remote.request.LoginRequest

class AuthRepository private constructor(
    private val apiService: ApiService
) {

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val request = LoginRequest(email, password)
            val response = apiService.loginUser(request)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d(TAG, "Failed to login: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }


    companion object {
        private const val TAG = "LoginViewModel"

        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            apiService: ApiService
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService)
            }.also { instance = it }
    }
}