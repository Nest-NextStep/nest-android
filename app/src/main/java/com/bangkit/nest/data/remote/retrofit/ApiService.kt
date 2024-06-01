package com.bangkit.nest.data.remote.retrofit

import com.bangkit.nest.data.remote.request.LoginRequest
import com.bangkit.nest.data.remote.response.LoginResponse
import retrofit2.http.*

interface ApiService {
    @POST("login")
    suspend fun loginUser (
        @Body request: LoginRequest
    ): LoginResponse
}