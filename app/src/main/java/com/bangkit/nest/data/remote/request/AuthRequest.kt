package com.bangkit.nest.data.remote.request

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val displayName: String,
    val email: String,
    val password: String
)

data class RefreshTokenRequest(
    val refreshToken: String
)