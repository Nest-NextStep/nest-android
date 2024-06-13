package com.bangkit.nest.ui.main.catalog.detail

import androidx.lifecycle.ViewModel
import com.bangkit.nest.data.repository.MajorRepository

class CatalogDetailViewModel(
    private val majorRepository: MajorRepository
) : ViewModel() {

    fun getDetailMajor(id: Int) =
        majorRepository.getDetailMajor(id)

}