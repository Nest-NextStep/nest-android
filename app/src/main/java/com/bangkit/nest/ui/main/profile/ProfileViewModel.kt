package com.bangkit.nest.ui.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.nest.data.repository.UserPrefRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userPrefRepository: UserPrefRepository,
) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            userPrefRepository.logout()
        }
    }

}