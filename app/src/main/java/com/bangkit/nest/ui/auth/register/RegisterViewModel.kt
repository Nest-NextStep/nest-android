package com.bangkit.nest.ui.auth.register

import androidx.lifecycle.ViewModel
import com.bangkit.nest.data.repository.AuthRepository

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun register(name: String, email: String, password: String) =
        authRepository.register(name, email, password)

}