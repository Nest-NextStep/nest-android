package com.bangkit.nest.data.remote.retrofit

import com.bangkit.nest.data.remote.request.LoginRequest
import com.bangkit.nest.data.remote.request.RegisterRequest

import com.bangkit.nest.data.remote.response.AllMajorResponse
import com.bangkit.nest.data.remote.response.DetailMajorResponse
import com.bangkit.nest.data.remote.response.FindMajorResponse
import com.bangkit.nest.data.remote.response.LoginResponse
import com.bangkit.nest.data.remote.response.RegisterResponse
import com.bangkit.nest.data.remote.response.QuestionsResponse
import com.bangkit.nest.data.remote.response.ResultsResponseItem
import okhttp3.ResponseBody
import retrofit2.Response
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
        @Path("id") id: Int
    ): DetailMajorResponse

    @GET("major/search")
    suspend fun findMajor (
        @Query("major_name") majorName: String
    ) : FindMajorResponse

    @GET("task/user/{username}")
    suspend fun getUserTasks(
        @Path("username") username: String
    ): Response<ResponseBody>

    @GET("assessment/{category}")
    suspend fun getQuestions(
        @Path("category") category: String
    ): QuestionsResponse

    @GET("/assessment/data/{username}")
    suspend fun getUserMajorResults(
        @Path("username") username: String
    ): List<ResultsResponseItem>

}
