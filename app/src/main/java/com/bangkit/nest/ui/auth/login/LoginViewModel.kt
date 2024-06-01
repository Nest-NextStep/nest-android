package com.bangkit.nest.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.nest.data.local.entity.UserModel
import com.bangkit.nest.data.repository.AuthRepository
import com.bangkit.nest.data.repository.UserPrefRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userPrefRepository: UserPrefRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    fun login(email: String, password: String) =
        authRepository.login(email, password)


    fun saveSession(user: UserModel) =
        viewModelScope.launch {
            userPrefRepository.saveSession(user)
        }

}