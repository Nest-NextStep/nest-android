package com.bangkit.nest.data.remote.retrofit

import android.util.Log
import com.bangkit.nest.data.Result
import com.bangkit.nest.data.remote.request.RefreshTokenRequest
import com.bangkit.nest.data.remote.response.TokenResponse
import com.bangkit.nest.data.repository.UserPrefRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor private constructor(
    private val userPrefRepository: UserPrefRepository
) : Interceptor {

    private lateinit var response: Response

    private val apiService by lazy {
        ApiConfig().getApiService()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = runBlocking {
            userPrefRepository.getToken()
        }

        if (token.isNotEmpty()) {
            val request = original.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()

            response = chain.proceed(request)

            if (response.code == 401) {

                response.close()

                val refreshToken = runBlocking { userPrefRepository.getRefreshToken() }

                val result = runBlocking {
                    refreshAccessToken(refreshToken)
                }

                when (result) {
                    is Result.Success -> {
                        runBlocking { userPrefRepository.saveToken(result.data.accessToken) }
                        val newToken = runBlocking { userPrefRepository.getToken() }
                        val newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer $newToken")
                            .build()
                        response = chain.proceed(newRequest)
                    }
                    is Result.Error -> {
                        Log.e(TAG, "Failed refreshing token: ${result.error}")
                    }
                    is Result.Loading -> {

                    }
                }
            }

        } else {
            response = chain.proceed(original)
        }

        return response
    }

    private suspend fun refreshAccessToken(refreshToken: String): Result<TokenResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val result = apiService.refreshToken(RefreshTokenRequest(refreshToken))
                Result.Success(result)
            } catch (e: Exception) {
                Log.e(TAG, "Error: ${e.message}")
                Result.Error(e.message ?: "Unknown error")
            }
        }
    }

    companion object {
        private const val TAG = "AuthInterceptor"

        @Volatile
        private var instance: AuthInterceptor? = null
        fun getInstance(
            userPrefRepository: UserPrefRepository
        ): AuthInterceptor =
            instance ?: synchronized(this) {
                instance ?: AuthInterceptor(userPrefRepository)
            }.also { instance = it }
    }
}