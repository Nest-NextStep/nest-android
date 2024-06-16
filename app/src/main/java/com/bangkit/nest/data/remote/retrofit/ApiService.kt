package com.bangkit.nest.data.remote.retrofit

import com.bangkit.nest.data.remote.request.ChangePasswordRequest
import com.bangkit.nest.data.remote.request.LoginRequest
import com.bangkit.nest.data.remote.request.ProfileRequest
import com.bangkit.nest.data.remote.request.RefreshTokenRequest
import com.bangkit.nest.data.remote.request.RegisterRequest
import com.bangkit.nest.data.remote.request.TaskRequest
import com.bangkit.nest.data.remote.response.AllMajorResponse
import com.bangkit.nest.data.remote.response.DetailMajorResponse
import com.bangkit.nest.data.remote.response.ProfileSuccessResponse
import com.bangkit.nest.data.remote.response.FindMajorResponse
import com.bangkit.nest.data.remote.response.LoginResponse
import com.bangkit.nest.data.remote.response.ProfileResponse
import com.bangkit.nest.data.remote.response.RegisterResponse
import com.bangkit.nest.data.remote.response.QuestionsResponse
import com.bangkit.nest.data.remote.response.ResultsResponseItem
import com.bangkit.nest.data.remote.response.StatusResponse
import com.bangkit.nest.data.remote.response.Task
import com.bangkit.nest.data.remote.response.TaskResponse
import com.bangkit.nest.data.remote.response.TestResultResponse
import com.bangkit.nest.data.remote.response.TokenResponse
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

    @POST("change-password")
    suspend fun changePassword (
        @Body request: ChangePasswordRequest
    ): ProfileSuccessResponse

    @GET("profile/{username}")
    suspend fun getProfileData (
        @Path("username") username: String
    ): ProfileResponse

    @PUT("profile/update/{username}")
    suspend fun editProfile (
        @Path("username") username: String,
        @Body request: ProfileRequest
    ): ProfileSuccessResponse

    @POST("refresh-token")
    suspend fun refreshToken (
        @Body request: RefreshTokenRequest
    ): TokenResponse

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
    ): TaskResponse

    @GET("task/{id}")
    suspend fun getDetailTaskById(
        @Path("id") taskId: String
    ): List<Task>

    @GET("task/completed/{username}")
    suspend fun getUserCompletedTasks(
        @Path("username") username: String
    ): List<Task>

    @POST("/task/{username}")
    suspend fun submitNewTask(
        @Path("username") username: String,
        @Body request: TaskRequest
    ): StatusResponse

    @PUT("/task/complete/{id}")
    suspend fun setTaskCompleted(
        @Path("id") id: Int,
    ): StatusResponse

    @PUT("/task/{id}")
    suspend fun updateTask(
        @Path("id") id: Int,
        @Body request: TaskRequest
    ): StatusResponse

    @DELETE("/task/{id}")
    suspend fun deleteTask(
        @Path("id") id: Int,
    ): StatusResponse

    @GET("assessment/{category}")
    suspend fun getQuestions(
        @Path("category") category: String
    ): QuestionsResponse

    @GET("/assessment/data/{username}")
    suspend fun getUserMajorResults(
        @Path("username") username: String
    ): List<ResultsResponseItem>

    @POST("assessment/result/{username}")
    suspend fun submitResults(
        @Path("username") username: String,
        @Body answers: Map<String, Int>
    ): TestResultResponse
}