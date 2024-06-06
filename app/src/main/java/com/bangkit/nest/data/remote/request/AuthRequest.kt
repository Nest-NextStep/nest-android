package com.bangkit.nest.data.remote.request

// TODO
data class LoginRequest(
    val email: String,
    val password: String
)

// TODO
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)