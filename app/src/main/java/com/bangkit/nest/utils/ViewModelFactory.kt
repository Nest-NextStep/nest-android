package com.bangkit.nest.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.nest.data.repository.AuthRepository
import com.bangkit.nest.data.repository.MajorRepository
import com.bangkit.nest.data.repository.AssessRepository
import com.bangkit.nest.data.repository.UserPrefRepository
import com.bangkit.nest.di.Injection
import com.bangkit.nest.ui.auth.login.LoginViewModel
import com.bangkit.nest.ui.auth.register.RegisterViewModel
import com.bangkit.nest.ui.main.MainViewModel
import com.bangkit.nest.ui.main.assess.AssessViewModel
import com.bangkit.nest.ui.main.assess.question.QuestionViewModel
import com.bangkit.nest.ui.main.catalog.CatalogViewModel
import com.bangkit.nest.ui.main.catalog.detail.CatalogDetailViewModel
import com.bangkit.nest.ui.main.profile.ProfileViewModel

class ViewModelFactory(
    private val userPrefRepository: UserPrefRepository,
    private val authRepository: AuthRepository,
    private val majorRepository: MajorRepository,
    private val assessRepository: AssessRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(authRepository) as T
            }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(authRepository) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(userPrefRepository) as T
            }

            modelClass.isAssignableFrom(CatalogViewModel::class.java) -> {
                CatalogViewModel(majorRepository) as T
            }

            modelClass.isAssignableFrom(CatalogDetailViewModel::class.java) -> {
                CatalogDetailViewModel(majorRepository) as T
            }

            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userPrefRepository) as T
            }

            modelClass.isAssignableFrom(AssessViewModel::class.java) -> {
                AssessViewModel(assessRepository) as T
            }

            modelClass.isAssignableFrom(QuestionViewModel::class.java) -> {
                QuestionViewModel(assessRepository) as T
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
                    Injection.provideAuthRepository(context),
                    Injection.provideMajorRepository(context),
                    Injection.provideAssessRepository(context)
                )
            }.also { instance = it }
    }
}