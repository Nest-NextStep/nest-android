package com.bangkit.nest.data.remote.response

import com.google.gson.annotations.SerializedName

// TODO
//data class LoginResponse(
//
//    @field:SerializedName("statusCode")
//    val statusCode: Int? = null,
//
//    @field:SerializedName("message")
//    val message: String? = null,
//
//    @field:SerializedName("username")
//    val username: String? = null
//)


// Nanti dihapus
data class LoginResponse(

    @field:SerializedName("loginResult")
    val loginResult: LoginResult? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class LoginResult(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null,

    @field:SerializedName("token")
    val token: String? = null
)

data class RegisterResponse(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)