package com.bangkit.nest.data.local.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bangkit.nest.data.local.entity.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = user.email
            preferences[USERNAME_KEY] = user.username
            preferences[TOKEN_KEY] = user.token
            preferences[REFRESH_TOKEN_KEY] = user.refreshToken
            preferences[IS_LOGIN_KEY] = true
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[EMAIL_KEY] ?: "",
                preferences[USERNAME_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[REFRESH_TOKEN_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false
            )
        }
    }

    suspend fun getRefreshToken(): String {
        val preferences = dataStore.data.first()
        return preferences[REFRESH_TOKEN_KEY]!!
    }

    suspend fun getToken(): String {
        val preferences = dataStore.data.first()
        return preferences[TOKEN_KEY] ?: return ""
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun saveMajors(majors: List<String>) {
        val namesString = Json.encodeToString(majors)
        dataStore.edit { preferences ->
            preferences[MAJORS_KEY] = namesString
        }
    }

    suspend fun getMajors(): List<String> {
        val preferences = dataStore.data.first()
        val namesString = preferences[MAJORS_KEY] ?: return emptyList()
        return Json.decodeFromString(namesString)
    }

    suspend fun getMajorId(): Int {
        val preferences = dataStore.data.first()
        return preferences[MAJOR_ID_KEY] ?: return -1
    }

    suspend fun saveMajorId(id: Int) {
        dataStore.edit { preferences ->
            preferences[MAJOR_ID_KEY] = id
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val EMAIL_KEY = stringPreferencesKey("email")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refreshToken")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")
        private val MAJORS_KEY = stringPreferencesKey("recommendedMajors")
        private val MAJOR_ID_KEY = intPreferencesKey("majorId")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}