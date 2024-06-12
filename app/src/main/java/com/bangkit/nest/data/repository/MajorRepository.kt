package com.bangkit.nest.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.nest.data.Result
import com.bangkit.nest.data.remote.response.AllMajorResponse
import com.bangkit.nest.data.remote.response.DetailMajorResponse
import com.bangkit.nest.data.remote.response.FindMajorResponse
import com.bangkit.nest.data.remote.response.MajorItem
import com.bangkit.nest.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MajorRepository private constructor(
    private val apiService: ApiService,
    private val userPrefRepository: UserPrefRepository
){
    fun getAllMajor(): LiveData<Result<AllMajorResponse>> = liveData {
        emit(Result.Loading)
        try {
            val userModel = runBlocking { userPrefRepository.getSession().first() }
            val username = userModel.username
            val response = apiService.getMajorByUsername(username)

            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d(TAG, "Failed to get all major: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getDetailMajor(majorId: Long): LiveData<Result<DetailMajorResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getMajorDetail(majorId)

            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d(TAG, "Failed to get detail major: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun findMajor(majorName: String): LiveData<Result<AllMajorResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.findMajor(majorName)

            // Get recommended majors
            val recommendedMajors = runBlocking { userPrefRepository.getMajors() }

            // Categorize the search result
            val majorRecommended = mutableListOf<MajorItem>()
            val majorsAll = mutableListOf<MajorItem>()
            response.majors.forEach { major ->
                if (recommendedMajors.any { it.equals(major.majorName, ignoreCase = true) }) {
                    majorRecommended.add(major)
                } else {
                    majorsAll.add(major)
                }
            }

            Log.d(TAG, recommendedMajors.toString())
            Log.d(TAG, majorRecommended.toString())


            val categorizedResponse = AllMajorResponse(
                majorsAll = majorsAll,
                majorRecommended = majorRecommended
            )

            emit(Result.Success(categorizedResponse))
        } catch (e: Exception) {
            Log.d(TAG, "Failed to get detail major: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        private const val TAG = "MajorRepository"

        @Volatile
        private var instance: MajorRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPrefRepository: UserPrefRepository
        ): MajorRepository =
            instance ?: synchronized(this) {
                instance ?: MajorRepository(apiService, userPrefRepository)
            }.also { instance = it }
    }
}