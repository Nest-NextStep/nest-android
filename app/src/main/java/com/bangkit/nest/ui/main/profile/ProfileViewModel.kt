package com.bangkit.nest.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.nest.data.Result
import com.bangkit.nest.data.remote.response.MajorItem
import com.bangkit.nest.data.remote.response.ProfileData
import com.bangkit.nest.data.repository.ProfileRepository
import com.bangkit.nest.data.repository.UserPrefRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userPrefRepository: UserPrefRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _profileData = MutableLiveData<ProfileData>()
    val profileData: LiveData<ProfileData> = _profileData

    private val _majorData = MutableLiveData<List<MajorItem>>()
    val majorData: LiveData<List<MajorItem>> = _majorData

    private val _state = MutableLiveData<Result<Unit>>()
    val state: LiveData<Result<Unit>> = _state

    private var isDataLoaded = false

    fun reloadProfileData() {
        isDataLoaded = false
    }

    fun getProfileData() {
        if (isDataLoaded) {
            _state.value = Result.Success(Unit)
            return
        }
        profileRepository.getProfileData().observeForever { result ->
            when (result) {
                is Result.Loading -> {
                    _state.value = Result.Loading
                }
                is Result.Success -> {
                    _profileData.value = result.data.profileData
                    _majorData.value = result.data.majorData
                    _state.value = Result.Success(Unit)
                    isDataLoaded = true
                }
                is Result.Error -> {
                    _state.value = Result.Error(result.error)
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPrefRepository.logout()
        }
    }
}