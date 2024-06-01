package com.bangkit.nest.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.nest.data.local.entity.UserModel
import com.bangkit.nest.data.repository.UserPrefRepository

class MainViewModel(
    private val userPrefRepository: UserPrefRepository,
) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return userPrefRepository.getSession().asLiveData()
    }
}