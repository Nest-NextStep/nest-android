package com.bangkit.nest.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.nest.data.local.entity.UserModel
import com.bangkit.nest.data.repository.UserPrefRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val userPrefRepository: UserPrefRepository,
) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return userPrefRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userPrefRepository.logout()
        }
    }
}