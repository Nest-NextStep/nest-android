package com.bangkit.nest.data.remote.retrofit

import android.util.Log
import com.bangkit.nest.data.Result
import com.bangkit.nest.data.repository.AuthRepository
import com.bangkit.nest.data.repository.MajorRepository
import com.bangkit.nest.data.repository.UserPrefRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor private constructor(
    private val userPrefRepository: UserPrefRepository
) : Interceptor {

    private lateinit var response: Response

    private val apiService by lazy {
        ApiConfig(this).getApiService()
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

//            if (response.code == 401) {
//
//                response.close()
//
//                runBlocking {
//                    apiService.refreshToken(token.toString()).collect {result ->
//                        is Result.Loading -> {
//
//                        }
//                        is Result.Success -> {
//                            userPrefRepository.saveToken(result.data)
//                        }
//                        is Result.Error -> {
//                            Log.e(TAG, "Failed refreshing token: " + result.error)
//                        }
//                    }
//                }
//
//                val newToken = runBlocking {
//                    userPrefRepository.getToken()
//                }
//
//                val newRequest = chain.request().newBuilder()
//                    .addHeader("Authorization", "Bearer $newToken")
//                    .build()
//
//                response = chain.proceed(newRequest)
//
//            }

        } else {
            response = chain.proceed(original)
        }

        return response
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