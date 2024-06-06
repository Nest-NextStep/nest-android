package com.bangkit.nest.data.remote.retrofit

import com.bangkit.nest.data.remote.request.LoginRequest
import com.bangkit.nest.data.remote.request.RegisterRequest
import com.bangkit.nest.data.remote.response.AllMajorResponse
import com.bangkit.nest.data.remote.response.DetailMajorResponse
import com.bangkit.nest.data.remote.response.FindMajorResponse
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

    @GET("major/{username}")
    suspend fun getMajorByUsername (
        @Path("username") username: String
    ): AllMajorResponse

    @GET("major/detail/{id}")
    suspend fun getMajorDetail (
        @Path("id") id: Long
    ): DetailMajorResponse


    @GET("major/search")
    suspend fun findMajor (
        @Query("major_name") majorName: String
    ) : FindMajorResponse
}