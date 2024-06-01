package com.bangkit.nest.di

import android.content.Context
import com.bangkit.nest.data.local.pref.UserPreference
import com.bangkit.nest.data.local.pref.dataStore
import com.bangkit.nest.data.remote.retrofit.ApiConfig
import com.bangkit.nest.data.repository.AuthRepository
import com.bangkit.nest.data.repository.UserPrefRepository

object Injection {
    fun provideUserPrefRepository(context: Context): UserPrefRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserPrefRepository.getInstance(pref)
    }

    fun provideAuthRepository(context: Context): AuthRepository {
        val apiService = ApiConfig.getApiService()
        return AuthRepository.getInstance(
            apiService
        )
    }
}