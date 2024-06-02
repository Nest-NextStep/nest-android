package com.bangkit.nest.data.local.entity

data class UserModel(
    val email: String,
    val username: String,
    val isLogin: Boolean = false
)
