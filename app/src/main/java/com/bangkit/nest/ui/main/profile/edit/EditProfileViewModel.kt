package com.bangkit.nest.ui.main.profile.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.nest.data.remote.request.ChangePasswordRequest
import com.bangkit.nest.data.remote.request.ProfileRequest
import com.bangkit.nest.data.repository.ProfileRepository
import com.bangkit.nest.data.repository.UserPrefRepository
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val userPrefRepository: UserPrefRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    fun editProfile(request: ProfileRequest) = profileRepository.editProfile(request)

    fun changePassword(request: ChangePasswordRequest) = profileRepository.changePassword(request)

    fun logout() {
        viewModelScope.launch {
            userPrefRepository.logout()
        }
    }
}