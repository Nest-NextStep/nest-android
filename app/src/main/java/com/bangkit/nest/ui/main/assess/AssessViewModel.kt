package com.bangkit.nest.ui.main.assess

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.nest.data.Result
import com.bangkit.nest.data.remote.response.ResultsResponse
import com.bangkit.nest.data.repository.AssessRepository

class AssessViewModel(private val assessRepository: AssessRepository) : ViewModel() {

    fun getUserMajorResults(): LiveData<Result<ResultsResponse>> {
        return assessRepository.getUserMajorResults()
    }
}
