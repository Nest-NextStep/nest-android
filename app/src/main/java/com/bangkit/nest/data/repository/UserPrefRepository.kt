package com.bangkit.nest.data.repository

import com.bangkit.nest.data.local.entity.UserModel
import com.bangkit.nest.data.local.pref.UserPreference
import kotlinx.coroutines.flow.Flow

class UserPrefRepository private constructor(
    private val userPreference: UserPreference
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserPrefRepository? = null
        fun getInstance(
            userPreference: UserPreference
        ): UserPrefRepository =
            instance ?: synchronized(this) {
                instance ?: UserPrefRepository(userPreference)
            }.also { instance = it }
    }
}