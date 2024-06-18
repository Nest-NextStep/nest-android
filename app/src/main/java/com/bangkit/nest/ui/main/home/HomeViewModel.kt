package com.bangkit.nest.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.nest.data.Result
import com.bangkit.nest.data.remote.response.MajorItem
import com.bangkit.nest.data.remote.response.ProfileData
import com.bangkit.nest.data.repository.MajorRepository
import com.bangkit.nest.data.repository.ProfileRepository
import com.bangkit.nest.data.repository.UserPrefRepository

class HomeViewModel(
    private val userPrefRepository: UserPrefRepository,
    private val profileRepository: ProfileRepository,
    private val majorRepository: MajorRepository
) : ViewModel() {

    private val _profileData = MutableLiveData<ProfileData>()
    val profileData: LiveData<ProfileData> = _profileData

    private val _majorData = MutableLiveData<List<MajorItem>>()
    val majorData: LiveData<List<MajorItem>> = _majorData

    private val _stateProfile = MutableLiveData<Result<Unit>>()
    val stateProfile: LiveData<Result<Unit>> = _stateProfile

    private val _stateMajor = MutableLiveData<Result<Unit>>()
    val stateMajor: LiveData<Result<Unit>> = _stateMajor

    init {
        getProfileData()
        getRandomMajor()
    }

    fun getProfileData() {
        profileRepository.getProfileData().observeForever { result ->
            when (result) {
                is Result.Loading -> {
                    _stateProfile.value = Result.Loading
                }
                is Result.Success -> {
                    _profileData.value = result.data.profileData
                    _stateProfile.value = Result.Success(Unit)
                }
                is Result.Error -> {
                    _stateProfile.value = Result.Error(result.error)
                }
            }
        }
    }

    fun getRandomMajor() {
        majorRepository.getRandomMajor(3).observeForever { result ->
            when (result) {
                is Result.Loading -> {
                    _stateMajor.value = Result.Loading
                }
                is Result.Success -> {
                    _majorData.value = result.data
                    _stateMajor.value = Result.Success(Unit)
                }
                is Result.Error -> {
                    _stateMajor.value = Result.Error(result.error)
                }
            }
        }
    }

}