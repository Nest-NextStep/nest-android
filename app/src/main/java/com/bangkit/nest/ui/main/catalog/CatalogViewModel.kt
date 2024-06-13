package com.bangkit.nest.ui.main.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.nest.data.repository.MajorRepository
import com.bangkit.nest.data.Result
import com.bangkit.nest.data.remote.response.MajorItem
import com.bangkit.nest.data.remote.response.ProfileData

class CatalogViewModel(
    private val majorRepository: MajorRepository
) : ViewModel() {

    private val _majorRecommended = MutableLiveData<List<MajorItem>>()
    val majorRecommended: LiveData<List<MajorItem>> = _majorRecommended

    private val _majorsAll = MutableLiveData<List<MajorItem>>()
    val majorsAll: LiveData<List<MajorItem>> = _majorsAll

    private val _state = MutableLiveData<Result<Unit>>()
    val state: LiveData<Result<Unit>> = _state

    private var isDataLoaded = false

    init {
        getAllMajor()
    }

    fun reloadAllMajorIfNeeded() {
        majorRepository.getAllMajor().observeForever { result ->
            when (result) {
                is Result.Success -> {
                    val newRecommendedMajor = result.data.majorRecommended
                    val newAllMajor = result.data.majorsAll

                    if (isNewData(newRecommendedMajor, newAllMajor)) {
                        _majorRecommended.value = result.data.majorRecommended
                        _majorsAll.value = result.data.majorsAll
                        _state.value = Result.Success(Unit)
                        isDataLoaded = true
                    }
                }
                is Result.Error -> {
                    _state.value = Result.Error(result.error)
                }
                is Result.Loading -> {

                }
            }
        }
    }

    private fun isNewData(newRecommendedMajor: List<MajorItem>, newAllMajor: List<MajorItem>): Boolean {
        return newRecommendedMajor != _majorRecommended.value || newAllMajor != _majorsAll.value
    }

    fun getAllMajor() {
        if (isDataLoaded) {
            _state.value = Result.Success(Unit)
            return
        }

        majorRepository.getAllMajor().observeForever { result ->
            when (result) {
                is Result.Loading -> {
                    _state.value = Result.Loading
                }
                is Result.Success -> {
                    _majorRecommended.value = result.data.majorRecommended
                    _majorsAll.value = result.data.majorsAll
                    _state.value = Result.Success(Unit)
                    isDataLoaded = true
                }
                is Result.Error -> {
                    _state.value = Result.Error(result.error)
                }
            }
        }
    }

    fun findMajor(majorName: String) =
        majorRepository.findMajor(majorName)
}