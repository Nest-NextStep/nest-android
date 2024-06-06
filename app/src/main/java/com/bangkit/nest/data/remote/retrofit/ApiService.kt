package com.bangkit.nest.data.remote.retrofit

import com.bangkit.nest.data.remote.request.LoginRequest
import com.bangkit.nest.data.remote.request.RegisterRequest
import com.bangkit.nest.data.remote.response.LoginResponse
import com.bangkit.nest.data.remote.response.RegisterResponse
import retrofit2.http.*

interface ApiService {
    @POST("login")
    suspend fun loginUser (
        @Body request: LoginRequest
    ): LoginResponse

    @POST("register")
    suspend fun registerUser (
        @Body request: RegisterRequest
    ): RegisterResponse

}