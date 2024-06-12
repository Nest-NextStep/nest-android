package com.bangkit.nest.data.local.entity

data class UserModel(
    val email: String,
    val username: String,
    val token: String,
    val isLogin: Boolean = false
)
