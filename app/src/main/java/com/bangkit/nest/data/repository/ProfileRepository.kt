package com.bangkit.nest.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.nest.data.Result
import com.bangkit.nest.data.remote.response.ProfileResponse
import com.bangkit.nest.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first
import retrofit2.HttpException

class ProfileRepository private constructor(
    private val apiService: ApiService,
    private val userPrefRepository: UserPrefRepository
) {
    fun getProfileData(): LiveData<Result<ProfileResponse>> = liveData {
        emit(Result.Loading)
        try {
            val username = userPrefRepository.getSession().first().username
            val response = apiService.getProfileData(username)

            emit(Result.Success(response))
        } catch (e: HttpException) {
            Log.e(TAG, "Failed to fetch user data: ${e.message()}")
            emit(Result.Error(e.message()))
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch user data: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        private const val TAG = "ProfileRepository"

        @Volatile
        private var instance: ProfileRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPrefRepository: UserPrefRepository
        ): ProfileRepository =
            instance ?: synchronized(this) {
                instance ?: ProfileRepository(apiService, userPrefRepository)
            }.also { instance = it }
    }
}