package com.bangkit.nest.ui.main.catalog.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.nest.data.Result
import com.bangkit.nest.data.remote.response.DetailMajorResponse
import com.bangkit.nest.data.repository.MajorRepository

class CatalogDetailViewModel(
    private val majorRepository: MajorRepository
) : ViewModel() {

    fun getDetailMajor() =
        majorRepository.getDetailMajor()

}