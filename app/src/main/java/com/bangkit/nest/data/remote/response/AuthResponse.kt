package com.bangkit.nest.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("uid")
    val uid: String,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("accessToken")
    val accessToken: String,

    @field:SerializedName("recommendedMajor")
    val recommendedMajor: List<MajorItem>? = null
)

data class RegisterResponse(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)