package com.bangkit.nest.ui.main.assess.question

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import com.bangkit.nest.data.Result
import com.bangkit.nest.data.remote.response.QuestionsResponse
import com.bangkit.nest.data.remote.response.ResultsResponse
import com.bangkit.nest.data.remote.response.TestResultResponse
import com.bangkit.nest.data.repository.AssessRepository

class QuestionViewModel(private val assessRepository: AssessRepository) : ViewModel() {

    fun getQuestions(category: String): LiveData<Result<QuestionsResponse>> {
        return assessRepository.getQuestions(category)
    }

    fun submitResults(answers: Map<String, Int>): LiveData<Result<TestResultResponse>> {
        return assessRepository.submitResults(answers)
    }
}
