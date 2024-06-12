package com.bangkit.nest.ui.main.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.nest.data.repository.MajorRepository
import com.bangkit.nest.data.Result
import com.bangkit.nest.data.remote.response.MajorItem

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

    fun saveMajorId(id: Int) = majorRepository.saveMajorId(id)
}