package com.bangkit.nest.ui.main.profile.edit

import androidx.lifecycle.ViewModel
import com.bangkit.nest.data.remote.request.ProfileRequest
import com.bangkit.nest.data.repository.ProfileRepository

class EditProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    fun editProfile(request: ProfileRequest) = profileRepository.editProfile(request)
}