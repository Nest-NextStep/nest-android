package com.bangkit.nest.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.nest.data.remote.response.LoginResponse
import com.bangkit.nest.data.remote.retrofit.ApiService
import com.bangkit.nest.data.Result
import com.bangkit.nest.data.local.entity.UserModel
import com.bangkit.nest.data.remote.request.LoginRequest
import com.bangkit.nest.data.remote.request.RegisterRequest
import com.bangkit.nest.data.remote.response.RegisterResponse

class AuthRepository private constructor(
    private val apiService: ApiService,
    private val userPrefRepository: UserPrefRepository
) {

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val request = LoginRequest(email, password)
            val response = apiService.loginUser(request)

            // save session
            userPrefRepository.saveSession(UserModel(email, response.username, response.accessToken, true))
            val majors: List<String> = response.recommendedMajor?.map { it.majorName.orEmpty() } ?: emptyList()
            userPrefRepository.saveMajors(majors)

            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d(TAG, "Failed to login: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun register(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val request = RegisterRequest(name, email, password)
            val response = apiService.registerUser(request)

            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d(TAG, "Failed to login: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }


    companion object {
        private const val TAG = "AuthRepository"

        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPrefRepository: UserPrefRepository
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService, userPrefRepository)
            }.also { instance = it }
    }
}