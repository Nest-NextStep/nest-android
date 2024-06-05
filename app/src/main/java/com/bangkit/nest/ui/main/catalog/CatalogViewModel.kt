package com.bangkit.nest.ui.main.catalog

import androidx.lifecycle.ViewModel
import com.bangkit.nest.data.repository.MajorRepository

class CatalogViewModel(
    private val majorRepository: MajorRepository
) : ViewModel() {

    fun getAllMajor() =
        majorRepository.getAllMajor()

    fun getDetailMajor(majorId: Long) =
        majorRepository.getDetailMajor(majorId)

    fun findMajor(majorName: String) =
        majorRepository.findMajor(majorName)
}