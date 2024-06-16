package com.bangkit.nest.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.nest.data.Result
import com.bangkit.nest.data.remote.request.ProfileRequest
import com.bangkit.nest.data.remote.response.EditProfileResponse
import com.bangkit.nest.data.remote.response.ProfileResponse
import com.bangkit.nest.data.remote.retrofit.ApiService
import com.bangkit.nest.utils.calculateAge
import kotlinx.coroutines.flow.first
import retrofit2.HttpException

class ProfileRepository private constructor(
    private val apiService: ApiService,
    private val userPrefRepository: UserPrefRepository
) {
    fun getProfileData(): LiveData<Result<ProfileResponse>> = liveData {
        emit(Result.Loading)
        try {
            val userModel = userPrefRepository.getSession().first()
            val username = userModel.username
            val email = userModel.email

            val response = apiService.getProfileData(username)
            response.profileData.userEmail = email

            emit(Result.Success(response))
        } catch (e: HttpException) {
            Log.e(TAG, "Failed to fetch user data: ${e.message()}")
            emit(Result.Error(e.message()))
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch user data: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun editProfile(request: ProfileRequest): LiveData<Result<EditProfileResponse>> = liveData {
        emit(Result.Loading)
        try {
            val userModel = userPrefRepository.getSession().first()
            val username = userModel.username
            request.username = username
            val isVoted = calculateAge(request.userBirthData!!) >= 17
            request.userVoted = if (isVoted) {
                "Ya"
            } else {
                "Tidak"
            }

            val response = apiService.editProfile(username, request)

            emit(Result.Success(response))
        } catch (e: HttpException) {
            Log.e(TAG, "Failed to update user data: ${e.message()}")
            emit(Result.Error(e.message()))
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update user data: ${e.message.toString()} ")
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