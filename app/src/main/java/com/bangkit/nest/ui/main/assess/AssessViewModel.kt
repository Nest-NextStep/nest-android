package com.bangkit.nest.ui.main.assess

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AssessViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is assess Fragment"
    }
    val text: LiveData<String> = _text
}