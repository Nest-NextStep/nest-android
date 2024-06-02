package com.bangkit.nest.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.nest.data.repository.AuthRepository
import com.bangkit.nest.data.repository.UserPrefRepository
import com.bangkit.nest.di.Injection
import com.bangkit.nest.ui.auth.login.LoginViewModel
import com.bangkit.nest.ui.auth.register.RegisterViewModel
import com.bangkit.nest.ui.main.MainViewModel

class ViewModelFactory(
    private val userPrefRepository: UserPrefRepository,
    private val authRepository: AuthRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userPrefRepository, authRepository) as T
            }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(authRepository) as T
            }

            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userPrefRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideUserPrefRepository(context),
                    Injection.provideAuthRepository(context)
                )
            }.also { instance = it }
    }
}