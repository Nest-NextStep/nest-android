package com.bangkit.nest.data.remote.retrofit

import com.bangkit.nest.data.remote.request.ChangePasswordRequest
import com.bangkit.nest.data.remote.request.LoginRequest
import com.bangkit.nest.data.remote.request.ProfileRequest
import com.bangkit.nest.data.remote.request.RefreshTokenRequest
import com.bangkit.nest.data.remote.request.RegisterRequest
import com.bangkit.nest.data.remote.response.AllMajorResponse
import com.bangkit.nest.data.remote.response.DetailMajorResponse
import com.bangkit.nest.data.remote.response.ProfileSuccessResponse
import com.bangkit.nest.data.remote.response.FindMajorResponse
import com.bangkit.nest.data.remote.response.LoginResponse
import com.bangkit.nest.data.remote.response.ProfileResponse
import com.bangkit.nest.data.remote.response.RegisterResponse
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
}