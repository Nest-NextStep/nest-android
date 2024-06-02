package com.bangkit.nest.ui.auth.login

import androidx.lifecycle.ViewModel
import com.bangkit.nest.data.repository.AuthRepository

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun login(email: String, password: String) =
        authRepository.login(email, password)

}