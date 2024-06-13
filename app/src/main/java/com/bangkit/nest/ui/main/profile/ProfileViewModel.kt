package com.bangkit.nest.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    fun changeToken() {
        viewModelScope.launch {
            userPrefRepository.saveToken("eyJhbGciOiJSUzI1NiIsImtpZCI6IjMzMDUxMThiZTBmNTZkYzA4NGE0NmExN2RiNzU1NjVkNzY4YmE2ZmUiLCJ0eXAiOiJKV1QifQ.eyJuYW1lIjoiZGV2aW5haGFpIiwiaXNzIjoiaHR0cHM6Ly9zZWN1cmV0b2tlbi5nb29nbGUuY29tL25lc3QtY2Fwc3RvbmVwcm9qZWN0IiwiYXVkIjoibmVzdC1jYXBzdG9uZXByb2plY3QiLCJhdXRoX3RpbWUiOjE3MTgyMTYwNTYsInVzZXJfaWQiOiI1eFpRM096Z3JkU2dBWUg2RGl3NW9wbTFkRzcyIiwic3ViIjoiNXhaUTNPemdyZFNnQVlINkRpdzVvcG0xZEc3MiIsImlhdCI6MTcxODIxNjA1NiwiZXhwIjoxNzE4MjE5NjU2LCJlbWFpbCI6ImRldmluYTFAZXhhbXBsZS5jb20iLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsiZW1haWwiOlsiZGV2aW5hMUBleGFtcGxlLmNvbSJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.p8y5DSPhY42z87Lz3WIoVrYrLTtAwp3Rxpdt7eZF78KAVTyCjkHlCTm62PuxAEd5H1OeLyeBI__sNCb7wehojq2oJYlRk8iUnrU7exK2cLxIwYstdKYQMsEXLprecppFFYdBsDsm3uO0UxoCa1Yx6NaVG8JHKWPc3ftLgaV3-powelhFjNhGKJJCpF14vAhH7a02KALYl77cJiBuTejjchb6v2di9LVJ2jU4jwoheJRieHQCVRye0klyEC9lhfiGkoab96oUfe3lS_ftxhZlEUDw5HOLLuanPIkRZqvCITllJcSlF9mc1OozOjVvxMhvlELuV9vkEtUDlHIkDdWjew")
        }
    }
}