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

    suspend fun saveToken(token: String) {
        userPreference.saveToken(token)
    }

    suspend fun getToken(): String {
        return userPreference.getToken()
    }

    suspend fun getIsProfileCompleted(): Boolean {
        return userPreference.getIsProfileCompleted()
    }

    suspend fun saveIsProfileCompleted(isCompleted: Boolean) {
        userPreference.saveIsProfileCompleted(isCompleted)
    }

    suspend fun getRefreshToken(): String {
        return userPreference.getRefreshToken()
    }

    suspend fun saveMajors(majors: List<String>) {
        userPreference.saveMajors(majors)
    }

    suspend fun getMajors(): List<String> {
        return userPreference.getMajors()
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