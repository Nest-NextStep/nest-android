package com.bangkit.nest.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.nest.data.Result
import com.bangkit.nest.data.remote.response.QuestionsResponse
import com.bangkit.nest.data.remote.response.ResultsResponse
import com.bangkit.nest.data.remote.response.TestResultResponse
import com.bangkit.nest.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class AssessRepository private constructor(
    private val apiService: ApiService,
    private val userPrefRepository: UserPrefRepository
) {

    fun getQuestions(category: String): LiveData<Result<QuestionsResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getQuestions(category)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d(TAG, "Failed to get questions: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUserMajorResults(): LiveData<Result<ResultsResponse>> = liveData {
        emit(Result.Loading)
        try {
            val userModel = runBlocking { userPrefRepository.getSession().first() }
            val username = userModel.username
            val response = apiService.getUserMajorResults(username)
            val resultsResponse = ResultsResponse(resultResponse = response)
            emit(Result.Success(resultsResponse))
        } catch (e: Exception) {
            Log.d(TAG, "Failed to get user major results: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun submitResults(answers: Map<String, Int>): LiveData<Result<TestResultResponse>> = liveData {
        emit(Result.Loading)
        try {
            val userModel = runBlocking { userPrefRepository.getSession().first() }
            val username = userModel.username ?: throw Exception("Username is null")
            val response = apiService.submitResults(username, answers)

            emit(Result.Success(response))
            val recommendedMajors = userPrefRepository.getMajors().toMutableList()
            if (response.majorName !in recommendedMajors) {
                response.majorName?.let { recommendedMajors.add(it) }
                userPrefRepository.saveMajors(recommendedMajors)
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error occurred"))
        }
    }

    companion object {
        private const val TAG = "AssessRepository"

        @Volatile
        private var instance: AssessRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPrefRepository: UserPrefRepository
        ): AssessRepository =
            instance ?: synchronized(this) {
                instance ?: AssessRepository(apiService, userPrefRepository)
            }.also { instance = it }
    }
}
